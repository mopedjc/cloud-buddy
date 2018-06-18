package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.AmazonSNSClient
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.resources.ListAllImages
import com.cloudsecurity.resources.ListAllUsers
import com.cloudsecurity.rules.sdk.ImagePublic
import com.cloudsecurity.rules.sdk.UserHasMFA

class IngestAccount implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
  String region = System.getenv('AWS_DEFAULT_REGION')
  UserHasMFA userHasMFA = new UserHasMFA()
  ImagePublic imagePublic = new ImagePublic()
  def listAllUsers = new ListAllUsers()
  def listAllImages = new ListAllImages()

  @Override
  ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) throws IOException {
    String accountId = context.invokedFunctionArn.split(":")[4]
    String region = context.invokedFunctionArn.split(":")[3]
    String topicARN = generateAlertTopicARN(accountId, region)

    List<Alert> mfaAlerts = listAllUsers.allResources().collect {
      userHasMFA.isFail(it.userName).withDetails(listAllUsers.details(it))
    }

    List<Alert> publicImagesAlerts = listAllImages.allResources().collect {
      imagePublic.isFail(it).withDetails(listAllImages.details(it))
    }

    List<Alert> alerts = mfaAlerts + publicImagesAlerts

    alerts.forEach {
      snsClient.publish(topicARN, it.toJson())
    }
    Response responseBody = Response.builder()
            .message(alerts.collect { it.toJson() }.toString())
            .input(input)
            .build()

    return ApiGatewayResponse.builder()
            .statusCode(200)
            .body(responseBody.message)
            .headers(['X-Powered-By': 'AWS Lambda & serverless'])
            .build()
  }

  def generateAlertTopicARN(String accountId, String region) {
    "arn:aws:sns:${region}:${accountId}:ingested-resources"
  }

  AmazonSNSClient snsClient = AmazonSNSAsyncClientBuilder.standard().build()
}

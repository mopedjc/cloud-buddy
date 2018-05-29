package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.cloudsecurity.rules.ImagePublic
import com.cloudsecurity.rules.Result
import com.cloudsecurity.rules.RootAccessKey
import com.cloudsecurity.rules.S3BucketGlobalDelete
import com.cloudsecurity.rules.S3BucketGlobalGet
import com.cloudsecurity.rules.S3BucketGlobalList
import com.cloudsecurity.rules.S3BucketGlobalPut
import com.cloudsecurity.rules.UserHasMFA

class ExecuteRule implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
  String region = System.getenv('AWS_DEFAULT_REGION')
  UserHasMFA userHasMFA = new UserHasMFA()
  S3BucketGlobalDelete s3BucketGlobalDelete = new S3BucketGlobalDelete()
  S3BucketGlobalGet s3BucketGlobalGet = new S3BucketGlobalGet()
  S3BucketGlobalList s3BucketGlobalList = new S3BucketGlobalList()
  S3BucketGlobalPut s3BucketGlobalPut = new S3BucketGlobalPut()
  RootAccessKey rootAccessKey = new RootAccessKey()
  ImagePublic imagePublic = new ImagePublic()
  def rulesMap = [ imagePublic, rootAccessKey, userHasMFA, s3BucketGlobalDelete, s3BucketGlobalGet, s3BucketGlobalList, s3BucketGlobalPut]

  @Override
  ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) throws IOException {
    println("received input: " + input)

    Map pathParameters = input.get('pathParameters')
    println("pathParametes is ${pathParameters}")
    String rule = pathParameters.get('rule')
    println("rule is ${rule.class}")

    def matchedRule = rulesMap.find { it.name == rule }

    println("matchedRule is ${matchedRule.name}")
    Response responseBody
    if (matchedRule && matchedRule.isResourceRule()) {
      Map params = input.get('queryStringParameters')
      String resource = params.get('resource')
      println("resource is ${resource}")
      Result result = matchedRule.isFail(resource)
      responseBody = Response.builder()
              .message(result.toJson())
              .input(input)
              .build()
    } else if (matchedRule) {
      Result result = matchedRule.isFail()
      responseBody = Response.builder()
              .message(result.toJson())
              .input(input)
              .build()
    } else {
      responseBody = Response.builder()
              .message("No matched rule for rule path ${pathParameters}")
              .input(input)
              .build()
    }


    return ApiGatewayResponse.builder()
            .statusCode(200)
            .body(responseBody.toJson())
            .headers(['X-Powered-By': 'AWS Lambda & serverless'])
            .build()
  }
}

package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import com.cloudsecurity.rules.sns.S3BucketGlobal
import com.cloudsecurity.rules.sns.SecurityGroupIngress
import com.cloudsecurity.rules.sns.UserHasMFA
import groovy.json.JsonSlurper

class ExecuteSNSRule implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
  String region = System.getenv('AWS_DEFAULT_REGION')

  List<UsingSNS> rulesMap = [new UserHasMFA(), new SecurityGroupIngress(), new S3BucketGlobal()]

  @Override
  ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) throws IOException {
    println("received input: " + input)

    Map pathParameters = input.get('pathParameters')
    println("pathParameters is ${pathParameters}")
    String rule = pathParameters.get('rule')
    println("rule is ${rule.class}")

    def matchedRule = rulesMap.find { it.name == rule }

    println("matchedRule is ${matchedRule.name}")
    Response responseBody
    if (matchedRule) {
      JsonSlurper slurper = new JsonSlurper()
      Map body = slurper.parseText(input.get('body')) as Map
      println("body is ${body}")
      Alert result = matchedRule.isFail(body)
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

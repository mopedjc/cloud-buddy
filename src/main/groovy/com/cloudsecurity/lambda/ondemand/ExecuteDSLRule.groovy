package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.usingdsl.AlwaysFail
import com.cloudsecurity.dsl.usingdsl.AlwaysFail3
import com.cloudsecurity.dsl.usingdsl.AlwaysPass

class ExecuteDSLRule implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
  String region = System.getenv('AWS_DEFAULT_REGION')
  def rulesMap = [new AlwaysFail(), new AlwaysPass(), new AlwaysFail3()]


  @Override
  ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) throws IOException {
    println("received input: " + input)
    Map pathParameters = input.get('pathParameters')
    println("pathParametes is ${pathParameters}")
    String rule = pathParameters.get('rule')
    println("rule is ${rule.class}")

    def matchedRule = rulesMap.find { it.name == rule }
    boolean isFailed = matchedRule.isFail()
    Alert result = new Alert()
    result.with {
      name = matchedRule.rule.name
      eventTime = Date.newInstance().toString()
      status = isFailed? "FAILED" : "PASSES"
      message = matchedRule.rule.description
    }

    Response responseBody = Response.builder()
              .message(result.toJson())
              .input(input)
              .build()

    return ApiGatewayResponse.builder()
            .statusCode(200)
            .body(responseBody.toJson())
            .headers(['X-Powered-By': 'AWS Lambda & serverless'])
            .build()
  }
}

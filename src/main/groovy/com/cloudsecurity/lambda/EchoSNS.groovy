package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent

class EchoSNS implements RequestHandler<SNSEvent, Void> {
  String defaultRegion = System.getenv('AWS_DEFAULT_REGION')

  @Override
  Void handleRequest(SNSEvent input, Context context) {
    println("functionArn: ${context.invokedFunctionArn}")
    String accountId = context.invokedFunctionArn.split(":")[4]
    String region = context.invokedFunctionArn.split(":")[3]

    println("SNSEvent : " + input.toString())
    println("in defaultRegion ${defaultRegion}, accountId: ${accountId}, region: ${region}, SNSEvent : " + input.toString())

    for (SNSEvent.SNSRecord next: input.getRecords()) {
      println("Accepted raw sns : " + next.SNS.message)
    }
  }

}

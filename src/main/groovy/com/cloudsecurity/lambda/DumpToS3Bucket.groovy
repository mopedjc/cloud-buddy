package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.cloudsecurity.dsl.Alert
import groovy.json.JsonSlurper

class DumpToS3Bucket implements RequestHandler<SNSEvent, Void> {

  String bucket = System.getenv('TARGET_S3_BUCKET')
  AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withForceGlobalBucketAccessEnabled(true).build()
  String region = System.getenv('AWS_DEFAULT_REGION')

  @Override
  Void handleRequest(SNSEvent input, Context context) {
    println("SNSEvent : " + input.toString())
    for (SNSEvent.SNSRecord next: input.getRecords()) {
      println("Accepted raw sns : " + next.SNS.message)
      JsonSlurper slurper = new JsonSlurper()
      Alert alert = slurper.parseText(next.SNS.message)
      s3Client.putObject(bucket, "region=${alert.region}/alert=${alert.name}/resource=${alert.resource}/${alert.resource}.json", alert.toJson())
    }
  }

}

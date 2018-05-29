package com.cloudsecurity.rules

import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class S3BucketGlobalDelete extends S3SimulationTrait implements ResourceRule {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  S3BucketGlobalDelete(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
    this.clientFactory = clientFactory
  }

  @Override
  String getResource(Object details) {
    details?.requestParameters.bucketName
  }

  String iamPolicy = """
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "*",
            "Resource": "*"
        }
    ]
}
"""

  @Override
  boolean isRelevant(String eventName) {
    eventName in [
            "DeleteBucketCors",
//            "DeleteBucketLifecycle",
            "DeleteBucketPolicy",
//            "DeleteBucketReplication",
//            "DeleteBucketTagging",
//            "DeleteBucketWebsite",
//            "CreateBucket",
            "PutBucketAcl",
            "PutBucketCors",
//            "PutBucketLifecycle",
            "PutBucketPolicy"
//            "PutBucketLogging",
//            "PutBucketNotification",
//            "PutBucketReplication",
//            "PutBucketTagging",
//            "PutBucketRequestPayment",
//            "PutBucketVersioning",
//            "PutBucketWebsite"
    ]
  }

  String getBucketLocation(String bucketName) {
    String location = s3Client.getBucketLocation(bucketName)

    if (location.equalsIgnoreCase('US')) {
      location = 'us-east-1'
    } else if (location.equalsIgnoreCase('EU')) {
      location = 'eu-west-1'
    }
    location
  }

  List<String> actionNames = [
          's3:DeleteBucket',
          's3:DeleteBucketPolicy',
          's3:DeleteBucketWebsite',
          's3:DeleteObject',
          's3:DeleteObjectVersion'
  ]

  @Override
  String getName() {
    return 'GlobalDelete'
  }
  
}

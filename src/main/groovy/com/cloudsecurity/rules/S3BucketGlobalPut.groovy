package com.cloudsecurity.rules

import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class S3BucketGlobalPut extends S3SimulationTrait implements ResourceRule {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  S3BucketGlobalPut(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
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
            "DeleteBucketLifecycle",
            "DeleteBucketPolicy",
            "DeleteBucketReplication",
            "DeleteBucketTagging",
            "DeleteBucketWebsite",
//            "CreateBucket",
            "PutBucketAcl",
            "PutBucketCors",
            "PutBucketLifecycle",
            "PutBucketPolicy",
            "PutBucketLogging",
            "PutBucketNotification",
            "PutBucketReplication",
            "PutBucketTagging",
            "PutBucketRequestPayment",
            "PutBucketVersioning",
            "PutBucketWebsite"
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

  @Override
  String getName() {
    return 'GlobalPut'
  }

  List<String> actionNames = [
          's3:PutAccelerateConfiguration',
          's3:PutBucketAcl',
          's3:PutBucketCORS',
          's3:PutBucketLogging',
          's3:PutBucketNotification',
          's3:PutBucketPolicy',
          's3:PutBucketRequestPayment',
          's3:PutBucketTagging',
          's3:PutBucketVersioning',
          's3:PutBucketWebsite',
          's3:PutLifecycleConfiguration',
          's3:PutObject',
          's3:PutObjectAcl',
          's3:PutObjectTagging',
          's3:PutObjectVersionAcl',
          's3:PutObjectVersionTagging',
          's3:PutReplicationConfiguration'
  ]
}

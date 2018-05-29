package com.cloudsecurity.rules

import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class S3BucketGlobalGet extends S3SimulationTrait implements ResourceRule {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  S3BucketGlobalGet(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
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
          's3:GetAccelerateConfiguration',
          's3:GetBucketAcl',
          's3:GetBucketCORS',
          's3:GetBucketLocation',
          's3:GetBucketLogging',
          's3:GetBucketNotification',
          's3:GetBucketPolicy',
          's3:GetBucketRequestPayment',
          's3:GetBucketTagging',
          's3:GetBucketVersioning',
          's3:GetBucketWebsite',
          's3:GetLifecycleConfiguration',
          's3:GetObject',
          's3:GetObjectAcl',
          's3:GetObjectTagging',
          's3:GetObjectTorrent',
          's3:GetObjectVersion',
          's3:GetObjectVersionAcl',
          's3:GetObjectVersionTagging',
          's3:GetObjectVersionTorrent',
          's3:GetReplicationConfiguration'
  ]

  @Override
  String getName() {
    return 'GlobalGet'
  }

}

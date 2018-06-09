package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AWSClientFactory
import com.cloudsecurity.dsl.util.EnvironmentVariables
import com.cloudsecurity.rules.sns.S3BucketGlobalSNSFilter

class S3BucketGlobalList extends S3SimulationTrait implements UsingSDK, S3BucketGlobalSNSFilter {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  S3BucketGlobalList(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
    this.clientFactory = clientFactory
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
  String getName() {
    return this.class.name
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

  List<String> actionNames = ['s3:ListBucket']
}

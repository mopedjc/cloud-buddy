package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.util.AbstractUsingSDK
import com.cloudsecurity.rules.sns.S3BucketGlobalFilterSNS

class S3BucketGlobalPut extends AbstractUsingSDK implements S3SimulationTrait {
  @Delegate S3BucketGlobalFilterSNS s3BucketGlobalFilterSNS = new S3BucketGlobalFilterSNS()

  @Override
  String getName() {
    return this.class.name
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

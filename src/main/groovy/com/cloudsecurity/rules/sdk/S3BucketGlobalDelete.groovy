package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.util.AbstractUsingSDK
import com.cloudsecurity.rules.sns.S3BucketGlobalFilterSNS

class S3BucketGlobalDelete extends AbstractUsingSDK implements S3SimulationTrait {

  @Delegate S3BucketGlobalFilterSNS s3BucketGlobalFilterSNS = new S3BucketGlobalFilterSNS()


  List<String> actionNames = [
          's3:DeleteBucket',
          's3:DeleteBucketPolicy',
          's3:DeleteBucketWebsite',
          's3:DeleteObject',
          's3:DeleteObjectVersion'
  ]

  @Override
  String getName() {
    return this.class.name
  }
  
}

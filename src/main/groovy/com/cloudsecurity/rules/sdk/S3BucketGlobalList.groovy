package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.util.AbstractUsingSDK
import com.cloudsecurity.rules.sns.S3BucketGlobalFilterSNS
import groovy.transform.CompileStatic

@CompileStatic
class S3BucketGlobalList extends AbstractUsingSDK implements S3SimulationTrait {
  @Delegate S3BucketGlobalFilterSNS s3BucketGlobalFilterSNS = new S3BucketGlobalFilterSNS()

  @Override
  String getName() {
    return this.class.name
  }

  List<String> actionNames = ['s3:ListBucket']
}

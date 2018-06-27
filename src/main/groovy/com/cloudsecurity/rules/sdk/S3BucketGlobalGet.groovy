package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.util.AbstractUsingSDK
import com.cloudsecurity.rules.sns.S3BucketGlobalFilterSNS

class S3BucketGlobalGet extends AbstractUsingSDK implements S3SimulationTrait {
  @Delegate S3BucketGlobalFilterSNS s3BucketGlobalFilterSNS = new S3BucketGlobalFilterSNS()

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
    return this.class.name
  }

}

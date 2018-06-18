package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import org.jetbrains.annotations.NotNull

class S3BucketGlobal extends S3BucketGlobalFilterSNS implements UsingSNS, FillDetailsTrait {

  @Override
  Alert isFail(@NotNull String snsString) {
    Map<String,?> sns = snsStringToMap(snsString)

    def snsDetails = sns.detail
    def resource = getResource(sns)
    def eventName = snsDetails?.eventName
    def region = snsDetails?.awsRegion
    def bucketPolicy = snsDetails?.requestParameters?.bucketPolicy

    if (eventName in ["PutBucketPolicy"] && isPublic(bucketPolicy)) {
      return fillDetails(new Alert(name: name, resource: resource, fail: true,
              message: "$resource has a public bucket policy", region: region), sns)
    }

    if (eventName in ["DeleteBucketPolicy"] || bucketPolicy == "") {
      return fillDetails(new Alert(name: name, resource: resource, fail: false, message: "$resource has no policy", region: region), sns)
    }
    return fillDetails(new Alert(name: name, resource: resource, fail: false,
            message: "$resource has policy but is not public", region: region), sns)
  }

  boolean isPublic(bucketPolicy) {
    bucketPolicy?.Statement?.any { isStatementPublic(it) }
  }
  boolean isStatementPublic(statement) {
    statement?.Effect == "Allow" && statement?.Principal == "*"
  }

  @Override
  String getName() {
    return 'S3BucketGlobal'
  }

}

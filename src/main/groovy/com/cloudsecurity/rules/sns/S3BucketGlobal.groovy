package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import org.jetbrains.annotations.NotNull

class S3BucketGlobal implements UsingSNS, FillDetailsTrait, S3BucketGlobalSNSFilter {

  @Override
  Alert isFail(@NotNull String snsString) {
    Map<String,?> sns = snsStringToMap(snsString)

    def snsDetails = sns.detail
    def resource = getResource(snsDetails)
    def eventName = snsDetails?.eventName
    def region = snsDetails?.awsRegion
    def bucketPolicy = snsDetails?.requestParameters?.bucketPolicy

    if (eventName in ["PutBucketPolicy"] && isPublic(bucketPolicy)) {
      return fillDetails(new Alert(name: name, resource: resource, fail: true, eventName: eventName,
              message: "$resource has a public bucket policy", region: region), sns)
    }

    if (eventName in ["DeleteBucketPolicy"] || bucketPolicy == "") {
      return fillDetails(new Alert(name: name, resource: resource, fail: false, eventName: eventName, message: "$resource has no policy", region: region), sns)
    }
    return fillDetails(new Alert(name: name, resource: resource, fail: false, eventName: eventName,
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
    return this.class.name
  }

}

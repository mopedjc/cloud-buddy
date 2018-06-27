package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import org.jetbrains.annotations.NotNull

class UserHasMFA extends UserHasMFAFilterSNS implements UsingSNS, FillDetailsTrait {
  String region = System.getenv('AWS_DEFAULT_REGION')

  @Override
  String getName() {
    return this.class.name
  }

  @Override
  Alert isFail(@NotNull String snsString) {
    Map<String,?> sns = snsStringToMap(snsString)

    def snsDetails = sns?.detail
    def resource = getResource(sns)
    def eventName = snsDetails?.eventName
    def region = snsDetails?.awsRegion
    if (eventName in ["EnableMFADevice"]) {
      return fillDetails(new Alert(name: name, resource: resource, fail: false,
              message: "$resource has MFA", region: region), sns)
    } else {
      return fillDetails(new Alert(name: name, resource: resource, fail: true,
              message: "$resource does not have MFA", region: region), sns)
    }
  }
}

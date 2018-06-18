package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import com.cloudsecurity.dsl.util.EnvironmentVariables
import org.jetbrains.annotations.NotNull

class UserHasMFA implements UsingSNS, FillDetailsTrait, UserHasMFASNSFilter {
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

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
              eventName: eventName,
              message: "$resource has MFA", region: region), sns)
    } else {
      return fillDetails(new Alert(name: name, resource: resource, fail: true, eventName: eventName,
              message: "$resource does not have MFA", region: region), sns)
    }
  }
}

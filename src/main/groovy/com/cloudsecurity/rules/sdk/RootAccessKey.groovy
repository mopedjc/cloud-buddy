package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AWSClientFactory
import com.cloudsecurity.dsl.util.EnvironmentVariables

class RootAccessKey implements UsingSDK {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  RootAccessKey(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
    this.clientFactory = clientFactory
  }

  @Override
  String getName() {
    return this.class.name
  }

  @Override
  Alert isFail(String resource) {

    int count = identityManagementClient.accountSummary.summaryMap['AccountAccessKeysPresent']
    Alert result = new Alert(name: name, eventTime: Date.newInstance().toString())
    result.fail = (count > 0)
    if (count > 0) {
      result.message = 'API root access keys on root account.'
    } else {
      result.message = 'No API root access keys found.'
    }
    result
  }
}

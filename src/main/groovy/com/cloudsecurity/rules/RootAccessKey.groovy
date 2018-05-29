package com.cloudsecurity.rules

import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class RootAccessKey implements AccountRule {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  RootAccessKey(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
    this.clientFactory = clientFactory
  }

  @Override
  String getName() {
    return 'RootAccessKey'
  }

  @Override
  Result isFail() {

    int count = identityManagementClient.accountSummary.summaryMap['AccountAccessKeysPresent']
    Result result = new Result(name: name, eventTime: Date.newInstance().toString())
    result.fail = (count > 0)
    if (count > 0) {
      result.message = 'API root access keys on root account.'
    } else {
      result.message = 'No API root access keys found.'
    }
    result
  }

}

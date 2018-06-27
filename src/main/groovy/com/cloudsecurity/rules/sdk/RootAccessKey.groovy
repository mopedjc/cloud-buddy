package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class RootAccessKey extends AbstractUsingSDK implements UsingSDK<String> {
  String region = System.getenv('AWS_DEFAULT_REGION')

  @Override
  String getName() {
    return 'RootAccessKey'
  }

  @Override
  Alert isFail(String resource = '') {
    int count = identityManagementClient?.accountSummary?.summaryMap?.AccountAccessKeysPresent
    Alert result = new Alert(name: name, details: [eventTime: Date.newInstance().toString()])
    result.fail = (count > 0)
    if (count > 0) {
      result.message = 'API root access keys on root account.'
    } else {
      result.message = 'No API root access keys found.'
    }
    result
  }
}

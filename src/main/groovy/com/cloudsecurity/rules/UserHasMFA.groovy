package com.cloudsecurity.rules

import com.amazonaws.services.identitymanagement.model.GetLoginProfileRequest
import com.amazonaws.services.identitymanagement.model.ListMFADevicesRequest
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class UserHasMFA implements ResourceRule {
  @Delegate AWSClientFactory clientFactory
  EnvironmentVariables environmentVariables = new EnvironmentVariables()

  UserHasMFA(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
    this.clientFactory = clientFactory
  }

  @Override
  String getResource(Object details) {
    details?.requestParameters.userName
  }

  @Override
  Result isFail(String userName, details = [:]) {
    GetLoginProfileRequest loginProfileRequest = new GetLoginProfileRequest(userName)
    // we don't care about the result of this call, just whether or not it completes
    identityManagementClient.getLoginProfile(loginProfileRequest)
    List<MFADevice> mfaDevices = identityManagementClient
            .listMFADevices(new ListMFADevicesRequest(userName: userName)).MFADevices

    boolean pass = mfaDevices.any { it.enableDate != null }
    if (!pass) {
      return new Result(name: name, resource: userName, fail: true, message: "$userName does not have MFA", region: 'us-east-1')
    } else {
      return new Result(name: name, resource: userName, fail: false, message: "$userName does have mfa)", region: 'us-east-1')
    }
  }

  @Override
  String getName() {
    return 'UserHasMFA'
  }

  @Override
  boolean isRelevant(String eventName) {
    eventName in [
           // "CreateUser",
           // "AttachUserPolicy",
           // "CreateAccessKey",
            "CreateLoginProfile"
    ]
  }

 }

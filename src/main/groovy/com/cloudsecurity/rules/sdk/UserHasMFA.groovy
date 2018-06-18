package com.cloudsecurity.rules.sdk

import com.amazonaws.services.identitymanagement.model.GetLoginProfileRequest
import com.amazonaws.services.identitymanagement.model.ListMFADevicesRequest
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AbstractUsingSDK
import com.cloudsecurity.rules.sns.UserHasMFAFilterSNS

class UserHasMFA extends AbstractUsingSDK implements UsingSDK<String> {
  String region = System.getenv('AWS_DEFAULT_REGION')
  @Delegate UserHasMFAFilterSNS userHasMFAFilterSNS = new UserHasMFAFilterSNS()

  @Override
  Alert isFail(String userName) {
    GetLoginProfileRequest loginProfileRequest = new GetLoginProfileRequest(userName)
    identityManagementClient.getLoginProfile(loginProfileRequest) // test if loginProfile exists
    List<MFADevice> mfaDevices = identityManagementClient
            .listMFADevices(new ListMFADevicesRequest(userName: userName)).MFADevices
    boolean pass = mfaDevices.any { it.enableDate != null }
    if (!pass) {
      return new Alert(name: name, resource: userName, fail: true, message: "$userName does not have MFA", region: region)
    } else {
      return new Alert(name: name, resource: userName, fail: false, message: "$userName does have mfa", region: region)
    }
  }

  @Override
  String getName() {
    'UserHasMFA'  // can't use this.class.typeName as unit test fails, with Mock com.cloudsecurity.rules.sdk.UserHasMFA$$EnhancerByCGLIB$$1db4124
  }
}

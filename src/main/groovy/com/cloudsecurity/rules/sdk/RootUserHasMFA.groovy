package com.cloudsecurity.rules.sdk

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class RootUserHasMFA extends AbstractUsingSDK implements UsingSDK<String> {
    String region = System.getenv('AWS_DEFAULT_REGION')

    @Override
    Alert isFail(String resource) {
        int count = identityManagementClient.accountSummary.summaryMap['AccountMFAEnabled']
        boolean pass = count > 0
        if (!pass) {
            return new Alert(name: name, resource: 'rootUser', fail: true, message: "rootUser does not have MFA", region: region)
        } else {
            return new Alert(name: name, resource: 'rootUser', fail: false, message: "rootUser does have mfa", region: region)
        }
    }

    @Override
    String getName() {
        'RootUserHasMFA'  // can't use this.class.typeName as unit test fails, with Mock com.cloudsecurity.rules.sdk.UserHasMFA$$EnhancerByCGLIB$$1db4124
    }


}

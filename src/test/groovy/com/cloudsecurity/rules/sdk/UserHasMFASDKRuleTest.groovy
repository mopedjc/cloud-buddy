package com.cloudsecurity.rules.sdk

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.GetLoginProfileResult
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult
import com.amazonaws.services.identitymanagement.model.LoginProfile
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.cloudsecurity.dsl.util.EnvironmentVariables

class UserHasMFASDKRuleTest extends spock.lang.Specification {

	def "user with mfa is a PASS"() {
		UserHasMFASDKRule userHasMFA = GroovySpy(UserHasMFASDKRule)
		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >>
				{
					println "In mock"
					return identityManagementClient
				}
		EnvironmentVariables environmentVariables = Mock(EnvironmentVariables)
		environmentVariables.getenv(_) >> 'test-region'
		userHasMFA.environmentVariables = environmentVariables
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [new MFADevice(userName: 'TestUser', enableDate: new Date())])

		expect:
			assert userHasMFA.isFail('johndoe').fail == false
			assert userHasMFA.isFail('johndoe').region == 'test-region'
	}

	def "user with out mfa is a FAIL"() {

		UserHasMFASDKRule userHasMFA = GroovySpy(UserHasMFASDKRule)

		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >>
				{
					println "In mock"
					return identityManagementClient
				}
		userHasMFA.environmentVariables = Mock(EnvironmentVariables)
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [])

		expect:
			assert userHasMFA.isFail('johndoe').fail == true
	}




}

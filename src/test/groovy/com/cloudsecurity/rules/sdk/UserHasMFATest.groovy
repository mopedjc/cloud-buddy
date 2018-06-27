package com.cloudsecurity.rules.sdk

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.GetLoginProfileResult
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult
import com.amazonaws.services.identitymanagement.model.LoginProfile
import com.amazonaws.services.identitymanagement.model.MFADevice

class UserHasMFATest extends spock.lang.Specification {

	def "user with mfa is a PASS"() {
		UserHasMFA userHasMFA = GroovySpy(UserHasMFA)
		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >>
				{
					println "In mock"
					return identityManagementClient
				}
		userHasMFA.region = 'test-region'
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [new MFADevice(userName: 'TestUser', enableDate: new Date())])

		expect:
			assert userHasMFA.isFail('johndoe').fail == false
			assert userHasMFA.isFail('johndoe').region == 'test-region'
	}

	def "user with out mfa is a FAIL"() {

		UserHasMFA userHasMFA = GroovySpy(UserHasMFA)
		userHasMFA.region = 'test-region'

		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >>
				{
					println "In mock"
					return identityManagementClient
				}
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [])

		expect:
			assert userHasMFA.isFail('johndoe').fail == true
	}




}

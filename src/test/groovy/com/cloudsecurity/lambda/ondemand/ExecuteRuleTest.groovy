package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.GetLoginProfileResult
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult
import com.amazonaws.services.identitymanagement.model.LoginProfile
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.cloudsecurity.rules.UserHasMFA
import spock.lang.Specification

class ExecuteRuleTest extends Specification {

	def "should match UserHasMFA"() {

		ExecuteRule executeRule = new ExecuteRule()

		UserHasMFA userHasMFA = GroovySpy(UserHasMFA)
		executeRule.rulesMap = [userHasMFA]
		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >> identityManagementClient
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [new MFADevice(userName: 'TestUser', enableDate: new Date())])


		executeRule.handleRequest(['pathParameters': ['rule': 'UserHasMFA'], 'queryStringParameters' : ['resource' : 'UserD']], null)

		expect:
			1 == 1
	}
}

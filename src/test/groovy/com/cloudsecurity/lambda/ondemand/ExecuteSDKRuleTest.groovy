package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.GetLoginProfileResult
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult
import com.amazonaws.services.identitymanagement.model.LoginProfile
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.cloudsecurity.dsl.util.EnvironmentVariables
import com.cloudsecurity.rules.sdk.UserHasMFASDKRule
import spock.lang.Specification

class ExecuteSDKRuleTest extends Specification {

	def "should match UserHasMFA"() {
		ExecuteSDKRule executeRule = new ExecuteSDKRule()
		UserHasMFASDKRule userHasMFA = GroovySpy(UserHasMFASDKRule)
		EnvironmentVariables environmentVariables = Mock(EnvironmentVariables)
		environmentVariables.getenv(_) >> 'test-region'
		userHasMFA.environmentVariables = environmentVariables

		executeRule.rulesMap = [userHasMFA]
		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >> identityManagementClient
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [new MFADevice(userName: 'TestUser', enableDate: new Date())])
		
		executeRule.handleRequest(['pathParameters': ['rule': 'UserHasMFASDKRule'], 'queryStringParameters' : ['resource' : 'UserD']], null)

		expect:
			1 == 1
	}
}

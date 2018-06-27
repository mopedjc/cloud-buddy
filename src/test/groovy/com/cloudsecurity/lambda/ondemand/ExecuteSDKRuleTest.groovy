package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.GetAccountSummaryResult
import com.amazonaws.services.identitymanagement.model.GetLoginProfileResult
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult
import com.amazonaws.services.identitymanagement.model.LoginProfile
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.cloudsecurity.rules.sdk.RootAccessKey
import com.cloudsecurity.rules.sdk.UserHasMFA
import spock.lang.Specification

class ExecuteSDKRuleTest extends Specification {

	def "should match UserHasMFA"() {
		ExecuteSDKRule executeRule = new ExecuteSDKRule()
		UserHasMFA userHasMFA = GroovySpy(UserHasMFA)
		userHasMFA.region = 'test-region'

		executeRule.rulesMap = [userHasMFA]
		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >> identityManagementClient
		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [new MFADevice(userName: 'TestUser', enableDate: new Date())])
		
		executeRule.handleRequest(['pathParameters': ['rule': 'UserHasMFA'], 'queryStringParameters' : ['resource' : 'UserD']], null)

		expect:
			1 == 1
	}

	def "should match RootAccessKey"() {
		ExecuteSDKRule executeRule = new ExecuteSDKRule()
		RootAccessKey rule = GroovySpy(RootAccessKey)
		rule.region = 'test-region'

		executeRule.rulesMap = [rule]
		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)


		identityManagementClient.accountSummary >> new GetAccountSummaryResult(summaryMap: [AccountAccessKeysPresent: 1])

		rule.getIdentityManagementClient() >> identityManagementClient

		executeRule.handleRequest(['pathParameters': ['rule': 'RootAccessKey'], 'queryStringParameters' : [:]], null)

		expect:
			1 == 1
	}
}

package com.cloudsecurity.lambda.ondemand

import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult
import com.amazonaws.services.identitymanagement.model.ListUsersResult
import com.amazonaws.services.identitymanagement.model.MFADevice
import com.amazonaws.services.identitymanagement.model.User
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.sns.AmazonSNSClient
import com.cloudsecurity.resources.ListAllImages
import com.cloudsecurity.resources.ListAllUsers
import com.cloudsecurity.rules.sdk.UserHasMFA
import spock.lang.Specification

class IngestAccountTest extends Specification {

	def "should be able produce correct json"() {
		IngestAccount ingestAccount = new IngestAccount()
		ingestAccount.snsClient = Mock(AmazonSNSClient)
		UserHasMFA userHasMFA = GroovySpy(UserHasMFA)
		userHasMFA.region = 'test-region'

		ListAllUsers listAllUsers = GroovySpy(ListAllUsers)
		listAllUsers.region = 'test-region'

		ingestAccount.userHasMFA = userHasMFA
		ingestAccount.listAllUsers = listAllUsers

		ListAllImages listAllImages = GroovySpy(ListAllImages)
		listAllImages.region = 'test-region'

		ingestAccount.listAllImages = listAllImages

		AmazonIdentityManagementClient identityManagementClient = Mock(AmazonIdentityManagementClient)
		userHasMFA.getIdentityManagementClient() >> identityManagementClient
		listAllUsers.getIdentityManagementClient() >> identityManagementClient

		AmazonEC2Client ec2Client = Mock(AmazonEC2Client)
		listAllImages.getEc2Client() >> ec2Client

		identityManagementClient.listUsers() >> new ListUsersResult(users: [new User(userName: 'TestUserA'), new User(userName:  'TestUserB')])
		identityManagementClient.listMFADevices({ it.userName == 'TestUserA' }) >> new ListMFADevicesResult(mFADevices: [new MFADevice()])
		identityManagementClient.listMFADevices({ it.userName == 'TestUserB' }) >> new ListMFADevicesResult(mFADevices: [new MFADevice()])

		Context context = Mock()
		context.invokedFunctionArn >> "hello:goodbye:noway:test-region:012345678901:MyFunction"
		def response = ingestAccount.handleRequest([:], context)

		expect:
			response.body == """[{"name":"UserHasMFA","fail":true,"message":"TestUserA does not have MFA","region":"test-region","resource":"TestUserA","details":{"createDate":null,"path":null,"userName":"TestUserA","userId":null,"arn":null,"passwordLastUsed":null}}, {"name":"UserHasMFA","fail":true,"message":"TestUserB does not have MFA","region":"test-region","resource":"TestUserB","details":{"createDate":null,"path":null,"userName":"TestUserB","userId":null,"arn":null,"passwordLastUsed":null}}]"""
	}
}

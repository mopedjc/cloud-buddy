package com.cloudsecurity.resources

import com.amazonaws.services.identitymanagement.model.User
import com.cloudsecurity.dsl.Alert
import spock.lang.Specification

class ListAllUsersTest extends Specification {

	def 'should be able to add details'() {
		ListAllUsers listAllUsers = new ListAllUsers()
		Alert alert = new Alert(region: 'TestRegion', name: 'MfaTest', resource: 'TestResource', details: [something: "TestSomething"])
		User user = Mock()
		user.arn >> 'TestArn'
		user.userName >> 'TestUserName'
		user.userId >> 'TestUserId'
		user.path >> 'TestPath'

		expect:
			alert.withDetails(listAllUsers.details(user)) ==
					new Alert(region: 'TestRegion', name: 'MfaTest', resource: 'TestResource', details: [something: "TestSomething", path: 'TestPath', userName: 'TestUserName', userId: 'TestUserId', arn: 'TestArn', createDate: null, passwordLastUsed: null])

	}
}

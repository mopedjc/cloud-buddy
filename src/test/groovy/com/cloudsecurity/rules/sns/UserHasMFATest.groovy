package com.cloudsecurity.rules.sns

import com.cloudsecurity.rules.sns.UserHasMFA
import spock.lang.Specification

class UserHasMFATest extends Specification {

	def "shouldFail"() {
		UserHasMFA rule = new UserHasMFA()

		expect:
			rule.isFail(createLoginProfile).fail == true
	}

	def "enableMFA should pass"() {
		UserHasMFA rule = new UserHasMFA()

		expect:
			rule.isFail(enableMFA).fail == false
	}

	def "deactivateMFA should fail"() {
		UserHasMFA rule = new UserHasMFA()

		expect:
			rule.isFail(deactivateMFA).fail == true
	}

	def createLoginProfile = """
{"version":"0","id":"db31ba12-2d25-38a9-1c71-f3c50f554eb2","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"012345678901","time":"2018-06-09T18:48:30Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"IAMUser","principalId":"AIDAII2GSDBBM6WZ2ZVXE","arn":"arn:aws:iam::0123456789012:user/CanCreateBuddyAccount","accountId":"0123456789012","accessKeyId":"AKIAIMHBORYPNL2BPPWQ","userName":"CanCreateBuddyAccount"},"eventTime":"2018-06-08T15:31:37Z","eventSource":"iam.amazonaws.com","eventName":"CreateLoginProfile","awsRegion":"us-east-1","sourceIPAddress":"131.228.197.7","userAgent":"aws-cli/1.11.73 Python/2.7.10 Darwin/17.4.0 botocore/1.5.36","requestParameters":{"passwordResetRequired":false,"userName":"Cli1"},"responseElements":{"loginProfile":{"passwordResetRequired":false,"userName":"Cli1","createDate":"Jun 8, 2018 3:31:37 PM"}},"requestID":"088da4db-6b31-11e8-9730-e72a87e8e1f0","eventID":"3d6fcd2f-1f5a-451b-a2e0-f285151ef77c","eventType":"AwsApiCall"}}
}}"""


	def createVirtualMFA = """
{"version":"0","id":"db31ba12-2d25-38a9-1c71-f3c50f554eb2","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"012345678901","time":"2018-06-09T18:48:30Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"Root","principalId":"012345678901","arn":"arn:aws:iam::012345678901:root","accountId":"012345678901","accessKeyId":"ASIAIUKA7DEBSJ4BFTLQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T18:19:31Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T18:48:30Z","eventSource":"iam.amazonaws.com","eventName":"CreateVirtualMFADevice","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"virtualMFADeviceName":"user1","path":"/"},"responseElements":{"virtualMFADevice":{"serialNumber":"arn:aws:iam::012345678901:mfa/user1"}},"requestID":"b3f6a9d4-6c15-11e8-b4c1-9d71c95ef006","eventID":"938b1c48-4152-4a74-aff9-e9c9759a5ffa","eventType":"AwsApiCall"}}
"""

	def enableMFA = """
{"version":"0","id":"ff4c796a-9afd-8f1b-40c9-3322943dcce9","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"012345678901","time":"2018-06-09T18:49:06Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"Root","principalId":"012345678901","arn":"arn:aws:iam::012345678901:root","accountId":"012345678901","accessKeyId":"ASIAIUKA7DEBSJ4BFTLQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T18:19:31Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T18:49:06Z","eventSource":"iam.amazonaws.com","eventName":"EnableMFADevice","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"userName":"user1","serialNumber":"arn:aws:iam::012345678901:mfa/user1"},"responseElements":null,"requestID":"c9bb2cba-6c15-11e8-aa27-d915b5266108","eventID":"943d8ec8-0e1d-498b-b156-2670073af839","eventType":"AwsApiCall"}}
"""

	def deleteVirtualMFA = """
{"version":"0","id":"c57f67a6-9eae-3d73-bb37-508078a8ce30","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"012345678901","time":"2018-06-09T18:54:20Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"Root","principalId":"012345678901","arn":"arn:aws:iam::012345678901:root","accountId":"012345678901","accessKeyId":"ASIAIUKA7DEBSJ4BFTLQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T18:19:31Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T18:54:20Z","eventSource":"iam.amazonaws.com","eventName":"DeleteVirtualMFADevice","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"serialNumber":"arn:aws:iam::012345678901:mfa/user1"},"responseElements":null,"requestID":"84cba96b-6c16-11e8-a5c0-1b5500a1ad1c","eventID":"eb01e566-f116-4181-9f54-37ff681c83e7","eventType":"AwsApiCall"}}
"""
	
	def deactivateMFA = """
{"version":"0","id":"8f950901-82db-6ad4-96fc-f50e9762dc97","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"012345678901","time":"2018-06-09T18:54:20Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"Root","principalId":"012345678901","arn":"arn:aws:iam::012345678901:root","accountId":"012345678901","accessKeyId":"ASIAIUKA7DEBSJ4BFTLQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T18:19:31Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T18:54:20Z","eventSource":"iam.amazonaws.com","eventName":"DeactivateMFADevice","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"userName":"user1","serialNumber":"arn:aws:iam::012345678901:mfa/user1"},"responseElements":null,"requestID":"84c567da-6c16-11e8-a5c0-1b5500a1ad1c","eventID":"6189974b-b3d5-4b08-a68c-64933866f60e","eventType":"AwsApiCall"}}
"""

}

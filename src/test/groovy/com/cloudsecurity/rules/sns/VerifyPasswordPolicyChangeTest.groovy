package com.cloudsecurity.rules.sns

import spock.lang.Specification

class VerifyPasswordPolicyChangeTest extends Specification {

	def "shouldFail"() {
		VerifyPasswordPolicyChange rule = new VerifyPasswordPolicyChange()

		expect:
			rule.isFail(sns1).fail == true
	}


	def sns1 = """
{"version":"0","id":"fd0428a5-ef71-2b0a-e31c-357b36eb330a","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"864374811737","time":"2018-06-15T01:13:54Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"Root","principalId":"864374811737","arn":"arn:aws:iam::864374811737:root","accountId":"864374811737","accessKeyId":"ASIAILIFBRET7WW73X2Q","userName":"jcazure2","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-15T00:55:15Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-15T01:13:54Z","eventSource":"iam.amazonaws.com","eventName":"UpdateAccountPasswordPolicy","awsRegion":"us-east-1","sourceIPAddress":"199.195.244.121","userAgent":"signin.amazonaws.com","requestParameters":{"requireLowercaseCharacters":false,"requireSymbols":false,"requireNumbers":false,"hardExpiry":false,"minimumPasswordLength":6,"requireUppercaseCharacters":false,"allowUsersToChangePassword":true},"responseElements":null,"requestID":"5ef1c671-7039-11e8-9e87-9d4ac7fa3889","eventID":"d6075109-d343-41dd-aae7-e320ca1c7cfa","eventType":"AwsApiCall"}}"""

	def sns2 = """
{"version":"0","id":"320be99a-18c0-0416-dcdc-bdc3342d4953","detail-type":"AWS API Call via CloudTrail","source":"aws.iam","account":"864374811737","time":"2018-06-15T01:14:12Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.02","userIdentity":{"type":"Root","principalId":"864374811737","arn":"arn:aws:iam::864374811737:root","accountId":"864374811737","accessKeyId":"ASIAILIFBRET7WW73X2Q","userName":"jcazure2","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-15T00:55:15Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-15T01:14:12Z","eventSource":"iam.amazonaws.com","eventName":"UpdateAccountPasswordPolicy","awsRegion":"us-east-1","sourceIPAddress":"199.195.244.121","userAgent":"signin.amazonaws.com","requestParameters":{"requireLowercaseCharacters":false,"requireSymbols":false,"requireNumbers":true,"hardExpiry":false,"minimumPasswordLength":6,"requireUppercaseCharacters":false,"allowUsersToChangePassword":true},"responseElements":null,"requestID":"697dd164-7039-11e8-b9c6-f96f70d25eef","eventID":"507c77ba-fa61-4908-9c5d-ebbed26e3c64","eventType":"AwsApiCall"}}
"""
}

package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import spock.lang.Specification

class ImagePublicTest extends Specification {

	def "On making image public, we should create a fail alert"() {
		ImagePublic imagePublic = new ImagePublic()
		Alert alert = imagePublic.isFail(publicSNS)

		expect:
			alert.fail == true

	}

	def "On making image private, we should create a pass alert"() {
		ImagePublic imagePublic = new ImagePublic()
		Alert alert = imagePublic.isFail(privateSNS)

		expect:
			alert.fail == false

	}

	def privateSNS = """
{"version":"0","id":"67e0ee28-5d7e-1c8f-d314-b788108fc1dc","detail-type":"AWS API Call via CloudTrail","source":"aws.ec2","account":"012345678901","time":"2018-06-12T01:54:47Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.05","userIdentity":{"type":"Root","principalId":"012345678901","arn":"arn:aws:iam::012345678901:root","accountId":"012345678901","accessKeyId":"ASIAJUF5NCPHJFHNUPCA","userName":"jcazure2","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-12T01:11:41Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-12T01:54:47Z","eventSource":"ec2.amazonaws.com","eventName":"ModifyImageAttribute","awsRegion":"us-east-1","sourceIPAddress":"172.58.139.66","userAgent":"signin.amazonaws.com","requestParameters":{"imageId":"ami-02b466201cbf1b617","launchPermission":{"remove":{"items":[{"group":"all"}]}},"attributeType":"launchPermission"},"responseElements":{"_return":true},"requestID":"bcd8be58-cf7c-41d2-95f9-e003668148cf","eventID":"00e9d8e0-e089-43db-b757-a5dd5c210c4f","eventType":"AwsApiCall"}}
"""
	def publicSNS = """
{"version":"0","id":"c21c0d9e-09e3-01ee-a1a3-6cd944ab33f2","detail-type":"AWS API Call via CloudTrail","source":"aws.ec2","account":"012345678901","time":"2018-06-12T01:17:53Z","region":"us-east-1","resources":[],
"detail":{"eventVersion":"1.05","userIdentity":{"type":"Root","principalId":"012345678901","arn":"arn:aws:iam::012345678901:root","accountId":"012345678901","accessKeyId":"ASIAJUF5NCPHJFHNUPCA","userName":"jcazure2","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-12T01:11:41Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-12T01:17:53Z","eventSource":"ec2.amazonaws.com","eventName":"ModifyImageAttribute","awsRegion":"us-east-1","sourceIPAddress":"172.58.139.66","userAgent":"signin.amazonaws.com","requestParameters":{"imageId":"ami-02b466201cbf1b617","launchPermission":{"add":{"items":[{"group":"all"}]}},"attributeType":"launchPermission"},"responseElements":{"_return":true},"requestID":"b62bb092-a6ea-4a4e-96ba-34d7533b648f","eventID":"3bcb33d2-ce17-4e98-a4a5-d71373819c90","eventType":"AwsApiCall"}}
"""
}

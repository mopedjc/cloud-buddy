package com.cloudsecurity.rules.sns

import spock.lang.Specification

class S3BucketGlobalTest extends Specification {

	def "Putting a public global policy shoud fail"() {
		S3BucketGlobal rule = new S3BucketGlobal()

		expect:
			rule.isFail(publicPutSNS).fail == true
	}

	def "Deleting a bucket policy should pass"() {
		S3BucketGlobal rule = new S3BucketGlobal()

		expect:
			rule.isFail(deleteBucketPolicy).fail == false
	}

	def "Putting a nonPublic bucket Policy should pass"() {
		S3BucketGlobal rule = new S3BucketGlobal()
		def alert = rule.isFail(putNonPublicPolicy)

		expect:
			alert.fail == false
			alert.region == 'us-east-1'
	}

	def publicPutSNS = """
{"version":"0","id":"b6d404a5-89b4-e0f0-a9e3-4dedecaf0682","detail-type":"AWS API Call via CloudTrail","source":"aws.s3","account":"012345678901","time":"2018-06-09T20:43:59Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.05","userIdentity":{"type":"IAMUser","principalId":"AIDAI43GIBMCOUZRFCU7U","arn":"arn:aws:iam::012345678901:user/johndoe","accountId":"012345678901","accessKeyId":"ASIAJ3SA4SDZWDHLYWEQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T20:43:39Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T20:43:59Z","eventSource":"s3.amazonaws.com","eventName":"PutBucketPolicy","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"bucketName":"testjcazurebucket","bucketPolicy":{"Version":"2012-10-17","Statement":[{"Sid":"Stmt1523832826961","Effect":"Allow","Principal":"*","Action":["s3:Get*","s3:List*","s3:Delete*"],"Resource":"arn:aws:s3:::testjcazurebucket/*"}],"Id":"Policy1523832833708"},"policy":[""]},"responseElements":null,"additionalEventData":{"vpcEndpointId":"vpce-b32f85da"},"requestID":"57E70CE5CA380E5B","eventID":"9396e340-fe75-48f7-a2b6-e0e6c6d32b4e","eventType":"AwsApiCall","vpcEndpointId":"vpce-b32f85da"}}
"""

	def deleteBucketPolicy = """
{"version":"0","id":"45d92967-3480-31f4-f6d7-27604d0ee7e9","detail-type":"AWS API Call via CloudTrail","source":"aws.s3","account":"012345678901","time":"2018-06-09T20:46:30Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.05","userIdentity":{"type":"IAMUser","principalId":"AIDAI43GIBMCOUZRFCU7U","arn":"arn:aws:iam::012345678901:user/johndoe","accountId":"012345678901","accessKeyId":"ASIAJ3SA4SDZWDHLYWEQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T20:43:39Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T20:46:30Z","eventSource":"s3.amazonaws.com","eventName":"DeleteBucketPolicy","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"bucketName":"testjcazurebucket","policy":[""]},"responseElements":null,"additionalEventData":{"vpcEndpointId":"vpce-6d72a204"},"requestID":"8771A58064240A89","eventID":"7c34e5f5-e711-427a-9186-72dbc300d098","eventType":"AwsApiCall","vpcEndpointId":"vpce-6d72a204"}}
"""

	def putNonPublicPolicy = """
{"version":"0","id":"49cab1dd-cc54-982e-a54b-10ac547c9a2d","detail-type":"AWS API Call via CloudTrail","source":"aws.s3","account":"012345678901","time":"2018-06-09T20:49:53Z","region":"us-east-1","resources":[],"detail":{"eventVersion":"1.05","userIdentity":{"type":"IAMUser","principalId":"AIDAI43GIBMCOUZRFCU7U","arn":"arn:aws:iam::012345678901:user/johndoe","accountId":"012345678901","accessKeyId":"ASIAJ3SA4SDZWDHLYWEQ","userName":"johndoe","sessionContext":{"attributes":{"mfaAuthenticated":"false","creationDate":"2018-06-09T20:43:39Z"}},"invokedBy":"signin.amazonaws.com"},"eventTime":"2018-06-09T20:49:53Z","eventSource":"s3.amazonaws.com","eventName":"PutBucketPolicy","awsRegion":"us-east-1","sourceIPAddress":"192.42.83.150","userAgent":"signin.amazonaws.com","requestParameters":{"bucketName":"testjcazurebucket","bucketPolicy":{"Version":"2012-10-17","Statement":[{"Sid":"Access-to-specific-VPCE-only","Effect":"Deny","Principal":"*","Action":"s3:*","Resource":["arn:aws:s3:::testjcazurebucket","arn:aws:s3:::testjcazurebucket/*"],"Condition":{"StringNotEquals":{"aws:sourceVpce":"vpce-1a2b3c4d"}}}],"Id":"Policy1415115909152"},"policy":[""]},"responseElements":null,"additionalEventData":{"vpcEndpointId":"vpce-e75c8f8e"},"requestID":"E9D4542BE68BB61B","eventID":"d5dc8f7e-3323-455d-94f5-6ef563eed329","eventType":"AwsApiCall","vpcEndpointId":"vpce-e75c8f8e"}}
"""
}

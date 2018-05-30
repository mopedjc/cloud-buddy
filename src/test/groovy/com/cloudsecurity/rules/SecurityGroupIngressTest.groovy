package com.cloudsecurity.rules

import groovy.json.JsonSlurper
import spock.lang.Specification

class SecurityGroupIngressTest extends Specification {

	def "shouldFail"() {

		SecurityGroupIngress securityGroupIngress = new SecurityGroupIngress()
		JsonSlurper slurper = new JsonSlurper()

		def details = slurper.parseText(snsMessage)

		expect:
			securityGroupIngress.isFail("sg-0fbaac03a0ed37aab", details).fail == true


	}
	def snsMessage = """
{
    "eventVersion": "1.05",
    "userIdentity": {
        "type": "Root",
        "principalId": "123456789012",
        "arn": "arn:aws:iam::123456789012:root",
        "accountId": "123456789012",
        "accessKeyId": "ASIAIFWOS6PE4CPQLRKQ",
        "sessionContext": {
            "attributes": {
                "mfaAuthenticated": "false",
                "creationDate": "2018-05-23T22:07:25Z"
            }
        },
        "invokedBy": "signin.amazonaws.com"
    },
    "eventTime": "2018-05-23T22:09:49Z",
    "eventSource": "ec2.amazonaws.com",
    "eventName": "AuthorizeSecurityGroupIngress",
    "awsRegion": "us-east-1",
    "sourceIPAddress": "38.104.243.166",
    "userAgent": "signin.amazonaws.com",
    "requestParameters": {
        "groupId": "sg-0fbaac03a0ed37aab",
        "ipPermissions": {
            "items": [
                {
                    "ipProtocol": "tcp",
                    "fromPort": 22,
                    "toPort": 22,
                    "groups": {},
                    "ipRanges": {},
                    "ipv6Ranges": {
                        "items": [
                            {
                                "cidrIpv6": "::/0",
                                "description": "open 22 anywhere"
                            }
                        ]
                    },
                    "prefixListIds": {}
                }
            ]
        }
    },
    "responseElements": {
        "requestId": "d2b051aa-7358-4430-b4b1-96db078daad6",
        "_return": true
    },
    "requestID": "d2b051aa-7358-4430-b4b1-96db078daad6",
    "eventID": "efae44db-44ac-4084-b15b-8828490c21c9",
    "eventType": "AwsApiCall",
    "recipientAccountId": "123456789012"
}"""
}

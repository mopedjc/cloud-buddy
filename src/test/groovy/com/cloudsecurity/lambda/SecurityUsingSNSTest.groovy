package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import spock.lang.Specification

class SecurityUsingSNSTest extends Specification {

//	def "should equal"() {
//
//		SNSEvent event = new SNSEvent()
//		Context context = Mock()
//		context.invokedFunctionArn >> "arn:aws:lambda:us-east-1:012345678901:function:aws-groovy-gradle-serverless-dev-echosns"
//		event.records = [new SNSEvent.SNSRecord(sns: new SNSEvent.SNS(message: message1))]
//		lambda.userHasMFA = Mock(UserHasMFA)
//
//		AmazonIdentityManagementClient identityManagementClient =  Mock(AmazonIdentityManagementClient)
//		lambda.userHasMFA.getIdentityManagementClient() >>
//				{
//					println "In mock"
//					return identityManagementClient
//				}
//
//		identityManagementClient.getLoginProfile(_) >> new GetLoginProfileResult(loginProfile: new LoginProfile(userName: 'TestUser'))
//		identityManagementClient.listMFADevices(_) >> new ListMFADevicesResult(mFADevices: [new MFADevice(userName: 'TestUser', enableDate: new Date())])
//		lambda.handleRequest(event, context)
//
//		expect:
//			assert 1 == 1
//	}

	def message1 = """{
    "version": "0",
    "id": "48f4be3d-44e2-f91e-d44c-f98ffb45bfaf",
    "detail-type": "AWS API Call via CloudTrail",
    "source": "aws.iam",
    "account": "012345678901",
    "time": "2018-04-28T19:00:41Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.02",
        "userIdentity": {
            "type": "IAMUser",
            "principalId": "AIDAI43GIBMCOUZRFCU7U",
            "arn": "arn:aws:iam::012345678901:user/johndoe",
            "accountId": "012345678901",
            "accessKeyId": "ASIAJ2LLME7WRWGKSYWQ",
            "userName": "johndoe",
            "sessionContext": {
                "attributes": {
                    "mfaAuthenticated": "false",
                    "creationDate": "2018-04-28T17:44:14Z"
                }
            },
            "invokedBy": "signin.amazonaws.com"
        },
        "eventTime": "2018-04-28T19:00:41Z",
        "eventSource": "iam.amazonaws.com",
        "eventName": "CreateUser",
        "awsRegion": "us-east-1",
        "sourceIPAddress": "172.58.140.80",
        "userAgent": "signin.amazonaws.com",
        "requestParameters": {
            "userName": "johndoeuser6"
        },
        "responseElements": {
            "user": {
                "path": "/",
                "arn": "arn:aws:iam::012345678901:user/johndoeuser6",
                "userId": "AIDAIYIYNWW2G7FGS2RZ4",
                "createDate": "Apr 28, 2018 7:00:41 PM",
                "userName": "johndoeuser6"
            }
        },
        "requestID": "727824d3-4b16-11e8-9854-37be5cc17f96",
        "eventID": "2810c33a-3164-4a00-bf49-3bf8d8367261",
        "eventType": "AwsApiCall"
    }
}"""


	SecurityUsingSNS lambda = new SecurityUsingSNS()

	def "should equal"() {
		Context context = Mock()
		context.invokedFunctionArn >> "arn:aws:lambda:us-east-1:012345678901:function:aws-groovy-gradle-serverless-dev-echosns"
		SNSEvent event = new SNSEvent()
		event.records = [ new SNSEvent.SNSRecord(sns: new SNSEvent.SNS(message: message1)) ]
		lambda.handleRequest(event, context)

		expect:
			assert 1 == 1
	}

	def message1a = """{
    "version": "0",
    "id": "77785fa2-0caf-ad59-d7f9-c3e69eb3b8f5",
    "detail-type": "AWS API Call via CloudTrail",
    "source": "aws.s3",
    "account": "012345678901",
    "time": "2018-04-25T23:03:40Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.05",
        "userIdentity": {
            "type": "IAMUser",
            "principalId": "AIDAI43GIBMCOUZRFCU7U",
            "arn": "arn:aws:iam::012345678901:user/johndoe",
            "accountId": "012345678901",
            "accessKeyId": "ASIAI4TNX4XOU7TXBFGQ",
            "userName": "johndoe",
            "sessionContext": {
                "attributes": {
                    "mfaAuthenticated": "false",
                    "creationDate": "2018-04-25T22:57:28Z"
                }
            }
        },
        "eventTime": "2018-04-25T23:03:40Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "PutBucketPolicy",
        "awsRegion": "us-east-1",
        "sourceIPAddress": "172.58.140.252",
        "userAgent": "[S3Console/0.4, aws-internal/3]",
        "requestParameters": {
            "bucketName": "jcazure2-dummy-bucket5",
            "bucketPolicy": {
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Sid": "Stmt1523832826961",
                        "Effect": "Allow",
                        "Principal": "*",
                        "Action": "s3:List*",
                        "Resource": "arn:aws:s3:::jcazure2-dummy-bucket6/*"
                    }
                ],
                "Id": "Policy1523832833708"
            },
            "policy": [
                ""
            ]
        },
        "responseElements": null,
        "additionalEventData": {
            "vpcEndpointId": "vpce-e75c8f8e"
        },
        "requestID": "46A08B0D9EF06F9B",
        "eventID": "6e47d4fd-ce1b-499a-9337-2e9eabf67b35",
        "eventType": "AwsApiCall",
        "vpcEndpointId": "vpce-e75c8f8e"
    }
}"""

	def message5 = """{
    "version": "0",
    "id": "77785fa2-0caf-ad59-d7f9-c3e69eb3b8f5",
    "detail-type": "AWS API Call via CloudTrail",
    "source": "aws.s3",
    "account": "012345678901",
    "time": "2018-04-25T23:03:40Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.05",
        "userIdentity": {
            "type": "IAMUser",
            "principalId": "AIDAI43GIBMCOUZRFCU7U",
            "arn": "arn:aws:iam::012345678901:user/johndoe",
            "accountId": "012345678901",
            "accessKeyId": "ASIAI4TNX4XOU7TXBFGQ",
            "userName": "johndoe",
            "sessionContext": {
                "attributes": {
                    "mfaAuthenticated": "false",
                    "creationDate": "2018-04-25T22:57:28Z"
                }
            }
        },
        "eventTime": "2018-04-25T23:03:40Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "PutBucketPolicy",
        "awsRegion": "us-east-1",
        "sourceIPAddress": "172.58.140.252",
        "userAgent": "[S3Console/0.4, aws-internal/3]",
        "requestParameters": {
            "bucketName": "jcazure2-dummy-bucket6",
            "bucketPolicy": {
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Sid": "Stmt1523832826961",
                        "Effect": "Allow",
                        "Principal": "*",
                        "Action": "s3:List*",
                        "Resource": "arn:aws:s3:::jcazure2-dummy-bucket6/*"
                    }
                ],
                "Id": "Policy1523832833708"
            },
            "policy": [
                ""
            ]
        },
        "responseElements": null,
        "additionalEventData": {
            "vpcEndpointId": "vpce-e75c8f8e"
        },
        "requestID": "46A08B0D9EF06F9B",
        "eventID": "6e47d4fd-ce1b-499a-9337-2e9eabf67b35",
        "eventType": "AwsApiCall",
        "vpcEndpointId": "vpce-e75c8f8e"
    }
}"""

	def message3 = """{
    "version": "0",
    "id": "e58a7484-7701-7d95-6401-2c231c12e36f",
    "detail-type": "AWS API Call via CloudTrail",
    "source": "aws.s3",
    "account": "012345678901",
    "time": "2018-04-25T22:06:44Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.05",
        "userIdentity": {
            "type": "Root",
            "principalId": "012345678901",
            "arn": "arn:aws:iam::012345678901:root",
            "accountId": "012345678901",
            "accessKeyId": "ASIAIUZTAG6UAIBGE2EQ",
            "sessionContext": {
                "attributes": {
                    "mfaAuthenticated": "false",
                    "creationDate": "2018-04-25T18:17:24Z"
                }
            },
            "invokedBy": "signin.amazonaws.com"
        },
        "eventTime": "2018-04-25T22:06:44Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "CreateBucket",
        "awsRegion": "us-east-1",
        "sourceIPAddress": "50.200.137.152",
        "userAgent": "signin.amazonaws.com",
        "requestParameters": {
            "bucketName": "jcazure2-dummy-bucket8"
        },
        "responseElements": null,
        "additionalEventData": {
            "vpcEndpointId": "vpce-6d72a204"
        },
        "requestID": "40A22949C685351C",
        "eventID": "a9eafd3e-0bec-4882-bfba-fefdb1a1f2b6",
        "eventType": "AwsApiCall",
        "vpcEndpointId": "vpce-6d72a204"
    }
}
"""

	def s3Contents ="""{
    "Records": [
        {
            "eventVersion": "1.05",
            "userIdentity": {
                "type": "Root",
                "principalId": "012345678901",
                "arn": "arn:aws:iam::012345678901:root",
                "accountId": "012345678901",
                "accessKeyId": "ASIAJWFK2UWL7IHKAFTA",
                "sessionContext": {
                    "attributes": {
                        "mfaAuthenticated": "false",
                        "creationDate": "2018-04-15T19:17:51Z"
                    }
                },
                "invokedBy": "signin.amazonaws.com"
            },
            "eventTime": "2018-04-16T03:16:48Z",
            "eventSource": "s3.amazonaws.com",
            "eventName": "CreateBucket",
            "awsRegion": "us-east-1",
            "sourceIPAddress": "68.184.181.195",
            "userAgent": "signin.amazonaws.com",
            "requestParameters": {
                "bucketName": "jcazure2-dummy-bucket4"
            },
            "responseElements": null,
            "additionalEventData": {
                "vpcEndpointId": "vpce-b32f85da"
            },
            "requestID": "6A29524171AAB979",
            "eventID": "87fee442-492d-4d99-872f-d9101a8af96b",
            "eventType": "AwsApiCall",
            "recipientAccountId": "012345678901",
            "vpcEndpointId": "vpce-b32f85da"
        }
    ]
}"""

	//def message1 = """{[{sns: {messageAttributes: {},signingCertUrl: https://sns.us-east-1.amazonaws.com/SimpleNotificationService-433026a4050d206028891664da859041.pem,messageId: 5cf3f6e5-cc28-52df-bad0-a5a976453454,message: {"s3Bucket":"jcazure2-write-only","s3ObjectKey":["AWSLogs/012345678901/CloudTrail/us-east-1/2018/04/16/012345678901_CloudTrail_us-east-1_20180416T2125Z_yZpaOh1piChHyQpy.json.gz"]},unsubscribeUrl: https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:012345678901:write-only:b64a5d1b-5535-4ee5-87fa-bc650cc56a0c,type: Notification,signatureVersion: 1,signature: MeqYUw0rh2y2/rSjenOk0n6DYukKHnd0JdZeJVY7AdMtsSCBRG8dhSEfrXVAAICekE3OVaJuJidHvjI2wMvFtosBbxeWbF4DGwtZTGBuLGmvkMOCPkx9F3LwxYvA5j+jbvYYjtBOU10J4MfuBYxm+ykpnYjDCN6LQQwz0gu6pxu2JpnrhNWwdwFGKPp3+f6ESYWpyUQ/OxUFbdMS78JhglCEWzq9SL81HrJnC0fl1HlA/aUOw9XSX65/lQTpNOoQMCSrhcujjHM09hffgUGqzxLlVylFFq40JBy/t1Hwup9idVHEMjuIxkn91LGqpw5kaYP0UyH0IB7vk5l87UZPlg==,timestamp: 2018-04-15T02:25:49.393Z,topicArn: arn:aws:sns:us-east-1:012345678901:write-only},eventVersion: 1.0,eventSource: aws:sns,eventSubscriptionArn: arn:aws:sns:us-east-1:012345678901:write-only:b64a5d1b-5535-4ee5-87fa-bc650cc56a0c}]}"""

	def message2 = """{
    "version": "0",
    "id": "551203c0-146b-067d-b45f-ff4eebb61f31",
    "detail-type": "AWS API Call via CloudTrail",
    "source": "aws.s3",
    "account": "012345678901",
    "time": "2018-04-25T18:21:31Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.05",
        "userIdentity": {
            "type": "Root",
            "principalId": "012345678901",
            "arn": "arn:aws:iam::012345678901:root",
            "accountId": "012345678901",
            "accessKeyId": "ASIAJY2PXBSMYKVUDWXA",
            "sessionContext": {
                "attributes": {
                    "mfaAuthenticated": "false",
                    "creationDate": "2018-04-25T18:17:24Z"
                }
            },
            "invokedBy": "cloudformation.amazonaws.com"
        },
        "eventTime": "2018-04-25T18:21:31Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "DeleteBucket",
        "awsRegion": "us-east-1",
        "sourceIPAddress": "cloudformation.amazonaws.com",
        "userAgent": "cloudformation.amazonaws.com",
        "errorCode": "BucketNotEmpty",
        "errorMessage": "The bucket you tried to delete is not empty",
        "requestParameters": {
            "bucketName": "moviesexample-dev-serverlessdeploymentbucket-p44zxbs9fw3k"
        },
        "responseElements": null,
        "requestID": "3DAA04C4AE1D22F1",
        "eventID": "2bf40e6d-7096-4dd9-89d4-d5049804b29f",
        "eventType": "AwsApiCall"
    }
}"""
}

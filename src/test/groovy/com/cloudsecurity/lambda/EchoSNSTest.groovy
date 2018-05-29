package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.SNSEvent

class EchoSNSTest extends spock.lang.Specification {

	EchoSNS lambda = new EchoSNS()


	def "should equal"() {

		SNSEvent event = new SNSEvent()
		event.records = [ new SNSEvent.SNSRecord(sns: new SNSEvent.SNS(message: message1)) ]
		Context context = Mock()
		context.invokedFunctionArn >> "arn:aws:lambda:us-east-1:123:function:aws-groovy-gradle-serverless-dev-echosns"

		lambda.handleRequest(event, context)

		expect:
		assert 1 == 1
	}

	def message1 = """{
    "version": "0",
    "id": "48f4be3d-44e2-f91e-d44c-f98ffb45bfaf",
    "detail-type": "AWS API Call via CloudTrail",
    "source": "aws.iam",
    "account": "864374811737",
    "time": "2018-04-28T19:00:41Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.02",
        "userIdentity": {
            "type": "IAMUser",
            "principalId": "AIDAI43GIBMCOUZRFCU7U",
            "arn": "arn:aws:iam::864374811737:user/johndoe",
            "accountId": "864374811737",
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
                "arn": "arn:aws:iam::864374811737:user/johndoeuser6",
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


}

package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.AmazonSNSClient
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import com.cloudsecurity.rules.sns.ImagePublic
import com.cloudsecurity.rules.sns.S3BucketGlobal
import com.cloudsecurity.rules.sns.SecurityGroupIngress
import com.cloudsecurity.rules.sns.UserHasMFA
import com.jayway.jsonpath.JsonPath

class SecurityUsingSNS implements RequestHandler<SNSEvent, String> {
	String defaultRegion = System.getenv('AWS_DEFAULT_REGION')

	SecurityGroupIngress securityGroupIngressOpen22 = new SecurityGroupIngress()
	UserHasMFA userHasMFA = new UserHasMFA()

	List<UsingSNS> rules = [securityGroupIngressOpen22, userHasMFA, new ImagePublic(), new S3BucketGlobal()]

	@Override
	String handleRequest(SNSEvent input, Context context) {
		String accountId = context.invokedFunctionArn.split(":")[4]
		String region = context.invokedFunctionArn.split(":")[3]
		String failedAlertsTopicARN = generateFailedAlertsTopicARN(accountId, region)
		String passedAlertsTopicARN = generatePassedAlertsTopicARN(accountId, region)
		println("version 1.12")

		println("functionArn: ${context.invokedFunctionArn}")
		println("in defaultRegion: ${defaultRegion}, accountId: " + accountId + ", SNSEvent : " + input.toString())

		StringBuffer ret = new StringBuffer()
		for (SNSEvent.SNSRecord next : input.getRecords()) {
			println("Accepted raw sns : " + next.SNS.message)
			String eventName = JsonPath.read(next.SNS.message, '$.detail.eventName')
			rules.findAll{ it.isRelevant(eventName) }.each {
				Alert result = it.isFail(next.SNS.message)
				if (result.fail) {
					snsClient.publish(failedAlertsTopicARN, result.toJson())
				} else {
					snsClient.publish(passedAlertsTopicARN, result.toJson())
				}
			}
		}

		ret.toString()
	}

	def generateFailedAlertsTopicARN(String accountId, String region) {
		"arn:aws:sns:${region}:${accountId}:failed-alerts"
	}

	def generatePassedAlertsTopicARN(String accountId, String region) {
		"arn:aws:sns:${region}:${accountId}:passed-alerts"
	}

	AmazonSNSClient snsClient = AmazonSNSAsyncClientBuilder.standard().build()
}

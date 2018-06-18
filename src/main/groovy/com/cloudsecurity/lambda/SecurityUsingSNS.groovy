package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.AmazonSNSClient
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import com.cloudsecurity.rules.sns.ImagePublic
import com.cloudsecurity.rules.sns.SecurityGroupIngress
import com.cloudsecurity.rules.sns.UserHasMFA
import com.jayway.jsonpath.JsonPath

class SecurityUsingSNS implements RequestHandler<SNSEvent, String> {
	String defaultRegion = System.getenv('AWS_DEFAULT_REGION')

	SecurityGroupIngress securityGroupIngressOpen22 = new SecurityGroupIngress()
	UserHasMFA userHasMFA = new UserHasMFA()

	List<UsingSNS> rules = [securityGroupIngressOpen22, userHasMFA, new ImagePublic()]

	@Override
	String handleRequest(SNSEvent input, Context context) {
		String accountId = context.invokedFunctionArn.split(":")[4]
		String region = context.invokedFunctionArn.split(":")[3]
		String topicARN = generateAlertTopicARN(accountId, region)
		println("version 1.1")

		println("functionArn: ${context.invokedFunctionArn}")
		println("in defaultRegion: ${defaultRegion}, accountId: " + accountId + ", SNSEvent : " + input.toString())

		StringBuffer ret = new StringBuffer()
		for (SNSEvent.SNSRecord next : input.getRecords()) {
			println("Accepted raw sns : " + next.SNS.message)
			String eventName = JsonPath.read(next.SNS.message, '$.detail.eventName')
			rules.findAll{ it.isRelevant(eventName) }.each {
				Alert result = it.isFail(next.SNS.message)
//				if (result.fail) {
				// TODO create a passed-alerts SNS topic
					snsClient.publish(topicARN, result.toJson())
//				}
			}
		}

		ret.toString()
	}

	def generateAlertTopicARN(String accountId, String region) {
		"arn:aws:sns:${region}:${accountId}:failed-alerts"
	}

	AmazonSNSClient snsClient = AmazonSNSAsyncClientBuilder.standard().build()
}

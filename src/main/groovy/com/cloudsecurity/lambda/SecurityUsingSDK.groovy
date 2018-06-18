package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.AmazonSNSClient
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.rules.sdk.ImagePublic
import com.cloudsecurity.rules.sdk.S3BucketGlobalDelete
import com.cloudsecurity.rules.sdk.S3BucketGlobalGet
import com.cloudsecurity.rules.sdk.S3BucketGlobalList
import com.cloudsecurity.rules.sdk.S3BucketGlobalPut
import com.cloudsecurity.rules.sdk.UserHasMFASDKRule
import groovy.json.JsonSlurper

class SecurityUsingSDK implements RequestHandler<SNSEvent, String> {
	String defaultRegion = System.getenv('AWS_DEFAULT_REGION')
	UserHasMFASDKRule userHasMFA = new UserHasMFASDKRule()
	S3BucketGlobalList s3BucketGlobalList = new S3BucketGlobalList()
	S3BucketGlobalGet s3BucketGlobalGet = new S3BucketGlobalGet()
	S3BucketGlobalDelete s3BucketGlobalDelete = new S3BucketGlobalDelete()
	S3BucketGlobalPut s3BucketGlobalPut = new S3BucketGlobalPut()
	ImagePublic imagePublic = new ImagePublic()
	List<UsingSDK> rules = [imagePublic, userHasMFA, s3BucketGlobalList, s3BucketGlobalGet, s3BucketGlobalPut, s3BucketGlobalDelete]

	@Override
	String handleRequest(SNSEvent input, Context context) {
		String accountId = context.invokedFunctionArn.split(":")[4]
		String region = context.invokedFunctionArn.split(":")[3]
		String topicARN = generateAlertTopicARN(accountId, region)

		println("functionArn: ${context.invokedFunctionArn}")
		println("in defaultRegion: ${defaultRegion}, accountId: " + accountId + ", SNSEvent : " + input.toString())
		JsonSlurper slurper = new JsonSlurper()

		StringBuffer ret = new StringBuffer()
		for (SNSEvent.SNSRecord next : input.getRecords()) {
			println("Accepted raw sns : " + next.SNS.message)
			def messageJson = slurper.parseText(next.SNS.message)
			def eventName = messageJson.detail.eventName
			def eventTime = messageJson.detail.eventTime
			rules.findAll{ it.isRelevant(eventName) }.each {
				UsingSDK usingSDK ->
				String resource = usingSDK.getResource(messageJson)
				Alert result = usingSDK.isFail(resource)
				fillDetails(result, messageJson.detail)

	//			if (result.fail) {
					snsClient.publish(topicARN, result.toJson())
	//			}
			}
		}

		ret.toString()
	}

	def fillDetails(Alert result, detail) {
		result.details['whoDidIt'] = detail?.userIdentity?.userName
		result.details['whoDidItArn'] = detail?.userIdentity?.arn
		result.details['eventID'] = detail?.eventID
		result.details['eventType'] = detail?.eventType
	}

	def generateAlertTopicARN(String accountId, String region) {
		"arn:aws:sns:${region}:${accountId}:failed-alerts"
	}


	AmazonSNSClient snsClient = AmazonSNSAsyncClientBuilder.standard().build()
}

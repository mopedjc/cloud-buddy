package com.cloudsecurity.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sns.AmazonSNSClient
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.FetchResourceUsingSDK
import com.cloudsecurity.dsl.FilterSNS
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.resources.GetImage
import com.cloudsecurity.resources.GetUser
import com.cloudsecurity.rules.sdk.ImagePublic
import com.cloudsecurity.rules.sdk.UserHasMFA
import com.cloudsecurity.rules.sns.ImagePublicFilterSNS
import com.cloudsecurity.rules.sns.UserHasMFAFilterSNS
import groovy.json.JsonSlurper

class SecurityUsingSDK implements RequestHandler<SNSEvent, String> {
    String defaultRegion = System.getenv('AWS_DEFAULT_REGION')
//    UserHasMFA userHasMFA = new UserHasMFA()
//    S3BucketGlobalList s3BucketGlobalList = new S3BucketGlobalList()
//    S3BucketGlobalGet s3BucketGlobalGet = new S3BucketGlobalGet()
//    S3BucketGlobalDelete s3BucketGlobalDelete = new S3BucketGlobalDelete()
//    S3BucketGlobalPut s3BucketGlobalPut = new S3BucketGlobalPut()
    ImagePublic imagePublic = new ImagePublic()
    List<Tuple<FetchResourceUsingSDK, UsingSDK, FilterSNS>> rules = [
            new Tuple(new GetImage(), imagePublic, new ImagePublicFilterSNS()),
            new Tuple(new GetUser(), new UserHasMFA(), new UserHasMFAFilterSNS())
            ]

    //, userHasMFA, s3BucketGlobalList, s3BucketGlobalGet, s3BucketGlobalPut, s3BucketGlobalDelete]

    @Override
    String handleRequest(SNSEvent input, Context context) {
        String accountId = context.invokedFunctionArn.split(":")[4]
        String region = context.invokedFunctionArn.split(":")[3]
        String failedAlertsTopicARN = generateFailedAlertsTopicARN(accountId, region)
        String passedAlertsTopicARN = generatePassedAlertsTopicARN(accountId, region)

        println("functionArn: ${context.invokedFunctionArn}")
        println("in defaultRegion: ${defaultRegion}, accountId: " + accountId + ", SNSEvent : " + input.toString())
        JsonSlurper slurper = new JsonSlurper()

        StringBuffer ret = new StringBuffer()
        for (SNSEvent.SNSRecord next : input.getRecords()) {
            println("Accepted raw sns : " + next.SNS.message)
            def messageJson = slurper.parseText(next.SNS.message)
            def eventName = messageJson.detail.eventName
            rules.findAll { it[2].isRelevant(eventName) }.each {
                Tuple tuple ->
                    UsingSDK usingSDK = tuple[1]
                    FetchResourceUsingSDK fetchResourceUsingSDK = tuple[0]
                    FilterSNS filterSNS = tuple[2]

                    String resourceName = filterSNS.getResource(messageJson)
                    def resource = fetchResourceUsingSDK.getResourceByName(resourceName)

                    Alert result = usingSDK.isFail(resource)
                    fillDetails(result, messageJson.detail)

                    if (result.fail) {
                        snsClient.publish(failedAlertsTopicARN, result.toJson())
                    } else {
                        snsClient.publish(passedAlertsTopicARN, result.toJson())
                    }
            }
        }

        ret.toString()
    }

    def fillDetails(Alert result, detail) {
        result.details['whoDidIt'] = detail?.userIdentity?.userName
        result.details['whoDidItArn'] = detail?.userIdentity?.arn
        result.details['eventID'] = detail?.eventID
        result.details['eventType'] = detail?.eventType
        result.details['eventTime'] = detail?.eventTime

    }

    def generateFailedAlertsTopicARN(String accountId, String region) {
        "arn:aws:sns:${region}:${accountId}:failed-alerts"
    }

    def generatePassedAlertsTopicARN(String accountId, String region) {
        "arn:aws:sns:${region}:${accountId}:passed-alerts"
    }

    AmazonSNSClient snsClient = AmazonSNSAsyncClientBuilder.standard().build()
}

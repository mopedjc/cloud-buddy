package com.cloudsecurity.rules.sdk

import com.amazonaws.services.identitymanagement.model.SimulateCustomPolicyRequest
import com.amazonaws.services.identitymanagement.model.SimulateCustomPolicyResult
import com.amazonaws.services.s3.model.BucketPolicy
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK

trait S3SimulationTrait implements UsingSDK<String> {
	String region = System.getenv('AWS_DEFAULT_REGION')
	String buddyAccountId = System.getenv('BUDDY_ACCOUNT_ID')

	String iamPolicy = """
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "*",
            "Resource": "*"
        }
    ]
}
"""

	String getBucketLocation(String bucketName) {
		String location = s3Client.getBucketLocation(bucketName)

		if (location.equalsIgnoreCase('US')) {
			location = 'us-east-1'
		} else if (location.equalsIgnoreCase('EU')) {
			location = 'eu-west-1'
		}
		location
	}

	@Override Alert isFail(String bucketName) {
		String bucketArn = "arn:aws:s3:::${bucketName}"
		String objectsInBucketArn = "arn:aws:s3:::${bucketName}/*"
		BucketPolicy bucketPolicy = s3Client.getBucketPolicy(bucketName)
		String bucketRegion = getBucketLocation(bucketName)
		String messageBody = "INFO, no bucket policy for ${bucketName}"
		Alert alert = new Alert()
		alert.name = name
		alert.resource = bucketName
		alert.message = messageBody
		alert.region = bucketRegion
		if (bucketPolicy.policyText) {
			SimulateCustomPolicyRequest policyRequest = new SimulateCustomPolicyRequest()
			policyRequest.setResourceArns([bucketArn, objectsInBucketArn])
			policyRequest.setResourcePolicy(bucketPolicy.policyText)
			policyRequest.setActionNames(actionNames)
			policyRequest.setPolicyInputList([iamPolicy])
			String callerArn = "arn:aws:iam::${buddyAccountId}:user/CloudBuddySimulationUser"
			policyRequest.setCallerArn(callerArn)
			SimulateCustomPolicyResult result = identityManagementClient.simulateCustomPolicy(policyRequest)

			boolean failed = result.evaluationResults.any {
				it.evalDecision == 'allowed' && it.matchedStatements.any { statement -> statement.sourcePolicyId =~ /(?i)ResourcePolicy/ }
			}
			alert.fail = failed
			if (failed) {
				alert.message = "${bucketName} has global ${name} permissions"
			} else {
				alert.message = "${bucketName} does not have global ${name} permissions"
			}
		}
		alert
	}
}

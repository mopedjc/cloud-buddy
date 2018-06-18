package com.cloudsecurity.rules.sdk

import com.amazonaws.services.identitymanagement.model.SimulateCustomPolicyRequest
import com.amazonaws.services.identitymanagement.model.SimulateCustomPolicyResult
import com.amazonaws.services.s3.model.BucketPolicy
import com.cloudsecurity.dsl.Alert

class S3SimulationTrait {
	Alert isFail(String bucketName) {
		String bucketArn = "arn:aws:s3:::${bucketName}"
		String objectsInBucketArn = "arn:aws:s3:::${bucketName}/*"
		BucketPolicy bucketPolicy = s3Client.getBucketPolicy(bucketName)
		String bucketRegion = getBucketLocation(bucketName)
		String messageBody = "INFO, no bucket policy for ${bucketName}"
		Alert bucketResult = new Alert()
		bucketResult.name = name
		bucketResult.resource = bucketName
		bucketResult.message = messageBody
		bucketResult.region = bucketRegion
		if (bucketPolicy.policyText) {
			SimulateCustomPolicyRequest policyRequest = new SimulateCustomPolicyRequest()
			policyRequest.setResourceArns([bucketArn, objectsInBucketArn])
			policyRequest.setResourcePolicy(bucketPolicy.policyText)
			policyRequest.setActionNames(actionNames)
			policyRequest.setPolicyInputList([iamPolicy])
			String callerArn = "arn:aws:iam::${environmentVariables.getenv('BUDDY_ACCOUNT_ID')}:user/CloudBuddySimulationUser"
			policyRequest.setCallerArn(callerArn)
			SimulateCustomPolicyResult result = identityManagementClient.simulateCustomPolicy(policyRequest)

			boolean failed = result.evaluationResults.any {
				it.evalDecision == 'allowed' && it.matchedStatements.any { statement -> statement.sourcePolicyId =~ /(?i)ResourcePolicy/ }
			}
			bucketResult.fail = failed
			if (failed) {
				bucketResult.message = "${bucketName} has global ${name} permissions"
			} else {
				bucketResult.message = "${bucketName} does not have global ${name} permissions"
			}
		}
		bucketResult
	}
}

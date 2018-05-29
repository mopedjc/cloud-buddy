package com.cloudsecurity.rules

import com.amazonaws.services.identitymanagement.model.SimulateCustomPolicyRequest
import com.amazonaws.services.identitymanagement.model.SimulateCustomPolicyResult
import com.amazonaws.services.s3.model.BucketPolicy

class S3SimulationTrait {
	Result isFail(String bucketName, details = [:]) {
		String bucketArn = "arn:aws:s3:::${bucketName}"
		String objectsInBucketArn = "arn:aws:s3:::${bucketName}/*"
		BucketPolicy bucketPolicy = s3Client.getBucketPolicy(bucketName)
		String bucketRegion = getBucketLocation(bucketName)
		String messageBody = "INFO, no bucket policy for ${bucketName}"
		Result bucketResult = new Result()
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
			String callerArn = environmentVariables.getenv('SIMULATION_CALLER_ARN')
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

package com.cloudsecurity

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider
import com.secops.util.AWSClientFactory

@Singleton
class STSAssumeRoleClientFactory extends AWSClientFactory {

	String getRoleArn() {
		environmentVariables.getenv('TARGET_ACCOUNT_ROLE')
	}

	AWSCredentialsProvider getCredentialsProvider() {
		if (null == stsAssumeRoleSessionCredentialsProvider) {
			stsAssumeRoleSessionCredentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, 'awsClientFactory').build()
		}
		stsAssumeRoleSessionCredentialsProvider
	}

	private STSAssumeRoleSessionCredentialsProvider stsAssumeRoleSessionCredentialsProvider
}

package com.cloudsecurity.dsl.util

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider
import com.cloudsecurity.dsl.util.AWSClientFactory

object STSAssumeRoleClientFactory : AWSClientFactoryImpl() {
	val roleArn = System.getenv("TARGET_ACCOUNT_ROLE")

	override fun getCredentialsProvider() : AWSCredentialsProvider? {
		if (null == stsAssumeRoleSessionCredentialsProvider) {
			stsAssumeRoleSessionCredentialsProvider = STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, "awsClientFactory").build()
		}
		return stsAssumeRoleSessionCredentialsProvider
	}

	private var stsAssumeRoleSessionCredentialsProvider: STSAssumeRoleSessionCredentialsProvider? = null
}

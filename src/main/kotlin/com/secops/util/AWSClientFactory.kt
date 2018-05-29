package com.secops.util

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder

abstract class AWSClientFactory {
    abstract fun getCredentialsProvider() : AWSCredentialsProvider
    val environmentVariables : EnvironmentVariables = EnvironmentVariables()
//
    val ec2Client: AmazonEC2 by lazy {
        CachingAmazonEC2(AmazonEC2ClientBuilder
            .standard()
            .withCredentials(getCredentialsProvider())
            .build()) as AmazonEC2
    }

    val s3Client: AmazonS3 by lazy {
        CachingAmazonS3(AmazonS3ClientBuilder
                .standard()
                .withCredentials(getCredentialsProvider())
                .withForceGlobalBucketAccessEnabled(true).build())
    }

    val identityManagementClient: AmazonIdentityManagement? by lazy {
        CachingAmazonIdentityManagement(AmazonIdentityManagementClientBuilder
                .standard()
                .withCredentials(getCredentialsProvider()).build())
    }

}

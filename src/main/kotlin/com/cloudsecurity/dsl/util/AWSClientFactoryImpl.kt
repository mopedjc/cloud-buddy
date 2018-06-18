package com.cloudsecurity.dsl.util

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder

abstract class AWSClientFactoryImpl : AWSClientFactory{
    override abstract fun getCredentialsProvider() : AWSCredentialsProvider?

    override val ec2Client: AmazonEC2 by lazy {
        CachingAmazonEC2(AmazonEC2ClientBuilder
            .standard()
            .withCredentials(getCredentialsProvider())
            .build()) as AmazonEC2
    }

    override val s3Client: AmazonS3 by lazy {
        CachingAmazonS3(AmazonS3ClientBuilder
                .standard()
                .withCredentials(getCredentialsProvider())
                .withForceGlobalBucketAccessEnabled(true).build())
    }

    override val identityManagementClient: AmazonIdentityManagement? by lazy {
        CachingAmazonIdentityManagement(AmazonIdentityManagementClientBuilder
                .standard()
                .withCredentials(getCredentialsProvider()).build())
    }

}

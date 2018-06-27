package com.cloudsecurity.dsl.util

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder

interface AWSClientFactory {
    fun getCredentialsProvider() : AWSCredentialsProvider?

    val ec2Client: AmazonEC2

    val s3Client: AmazonS3

    val identityManagementClient: AmazonIdentityManagement?

}

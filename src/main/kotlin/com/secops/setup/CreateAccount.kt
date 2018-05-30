package com.secops.setup

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.identitymanagement.model.AttachRolePolicyRequest
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest
import com.amazonaws.services.identitymanagement.model.CreateUserRequest
import com.amazonaws.services.identitymanagement.model.PutRolePolicyRequest
import com.amazonaws.services.organizations.model.CreateAccountRequest
import com.amazonaws.services.organizations.model.DescribeCreateAccountStatusRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


fun main(args : Array<String>) {
    //createAccount()

    //createRole()
    createSecurityRoleInTargetAccount()
    createSimulationUser()

}

// SIMULATION_CALLER_ARN: arn:aws:iam::187091375885:user/TestUser
fun createSimulationUser() {
    
    val client = AmazonIdentityManagementClientBuilder.standard().build()
    client.createUser(CreateUserRequest().withUserName("TestUser"))
}

/**
 * BUDDY_ACCOUNT_ID in config.dev.yml
 */
fun getBuddyAccountId(): String {
    val config = loadFromFile(Paths.get("config.dev.yml"))
    return config.buddyAccountId
}

fun getBuddyAccountName(): String {
    val config = loadFromFile(Paths.get("config.dev.yml"))
    return "${config.accountName}secops"
}


fun getBuddyEmail() : String {
    val config = loadFromFile(Paths.get("config.dev.yml"))
    return config.buddyEmail
}

fun loadFromFile(path: Path): Config {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, Config::class.java)
    }
}


fun createSecurityRoleInTargetAccount() {
    val assumePolicy = """{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::${getBuddyAccountId()}:root"
      },
      "Action": "sts:AssumeRole",
      "Condition": {}
    }
  ]
}"""
    val client = AmazonIdentityManagementClientBuilder.standard().build()
    val roleName = "ForCloudBuddySecurityAuditRole"
    val createRoleResult = client.createRole(CreateRoleRequest().withRoleName(roleName)
            .withAssumeRolePolicyDocument(assumePolicy))

    client.attachRolePolicy(AttachRolePolicyRequest().withRoleName(roleName).withPolicyArn("arn:aws:iam::aws:policy/SecurityAudit"))

    val inlinePolicy = """{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "iam:*",
                "s3:ListAllMyBuckets",
                "logs:*",
                "s3:HeadBucket",
                "s3:ListObjects"
            ],
            "Resource": "*"
        },
        {
            "Sid": "VisualEditor1",
            "Effect": "Allow",
            "Action": [
                "s3:ListBucketByTags",
                "s3:GetLifecycleConfiguration",
                "s3:GetBucketTagging",
                "s3:GetInventoryConfiguration",
                "s3:GetObjectVersionTagging",
                "s3:ListBucketVersions",
                "s3:GetBucketLogging",
                "s3:ListBucket",
                "s3:GetAccelerateConfiguration",
                "s3:GetBucketPolicy",
                "s3:GetObjectVersionTorrent",
                "s3:GetObjectAcl",
                "s3:GetBucketRequestPayment",
                "s3:GetObjectVersionAcl",
                "s3:GetObjectTagging",
                "s3:GetMetricsConfiguration",
                "s3:GetIpConfiguration",
                "s3:ListBucketMultipartUploads",
                "s3:GetBucketWebsite",
                "s3:GetBucketVersioning",
                "s3:GetBucketAcl",
                "s3:GetBucketNotification",
                "s3:GetReplicationConfiguration",
                "s3:ListMultipartUploadParts",
                "s3:GetObject",
                "s3:GetObjectTorrent",
                "s3:GetBucketCORS",
                "s3:GetAnalyticsConfiguration",
                "s3:GetObjectVersionForReplication",
                "s3:GetBucketLocation",
                "s3:GetObjectVersion"
            ],
            "Resource": [
                "arn:aws:s3:::*/*"
            ]
        }
    ]
}"""

    client.putRolePolicy(PutRolePolicyRequest().withRoleName(roleName).withPolicyName("MyInlinePolicy").withPolicyDocument(inlinePolicy))
}




fun createAccount() {
    val client = com.amazonaws.services.organizations.AWSOrganizationsClientBuilder.standard().build()
    val result = client.createAccount(CreateAccountRequest()
            .withAccountName(getBuddyAccountName())
            .withEmail(getBuddyEmail())
            .withRoleName("OrganizationAdminRole"))
    var finished = false
    while (!finished) {
        val status = client.describeCreateAccountStatus(DescribeCreateAccountStatusRequest().withCreateAccountRequestId(result.createAccountStatus.id))
        if (status.createAccountStatus.state.contentEquals("SUCCEEDED")) {
            finished = true
        }
    }
    println("Finished, creating account ${result.createAccountStatus.accountName}")
}


/**
 * Not sure if we need this, as serverless handles this
 */
fun createBuddyRole() {
    val assumePolicy = """{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::${getBuddyAccountId()}:root"
      },
      "Action": "sts:AssumeRole",
      "Condition": {}
    }
  ]
}"""
    val client = AmazonIdentityManagementClientBuilder.standard().build()
    client.createRole(CreateRoleRequest().withRoleName("Assume${getBuddyAccountName()}")
            .withAssumeRolePolicyDocument(assumePolicy))
}
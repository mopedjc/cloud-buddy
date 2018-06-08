package com.secops.setup

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder
import com.amazonaws.services.cloudformation.model.*
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import com.amazonaws.waiters.WaiterParameters
import com.secops.util.EnvironmentVariables


fun main(args: Array<String>) {
    oneclickRunCloudFormation()
}

fun oneclickRunCloudFormation() {
    validateCloudFormationInTargetAccount()
    runLastCloudFormationInBuddyAccount()
   // Thread.sleep(3 * 60 * 1000)    // TODO query cloudformation status
    runLastCloudFormationInTargetAccount()
}

fun String.asResource(work: (String) -> Unit) {
    val content = EnvironmentVariables().javaClass.classLoader.getResource(this).readText()
    work(content)
}

fun validateCloudFormationInTargetAccount() {
    val client: AmazonCloudFormation = AmazonCloudFormationClientBuilder.standard().build()
    "cloudformation-for-target-account-stack.json".asResource { templateBody ->
        val validateTemplateResult = client.validateTemplate(ValidateTemplateRequest().withTemplateBody(templateBody))
        println(validateTemplateResult.description)
    }
}

fun runLastCloudFormationInTargetAccount() {
    val client: AmazonCloudFormation = AmazonCloudFormationClientBuilder.standard().build()
    "cloudformation-for-target-account-stack.json".asResource { templateBody ->

        val stackName = "cloud-buddy-for-target-account-dev"

        val createRequest = CreateStackRequest().withStackName(stackName)
                .withTemplateBody(templateBody)
                .withCapabilities(Capability.CAPABILITY_IAM, Capability.CAPABILITY_NAMED_IAM)
                .withParameters(
                        Parameter().withParameterKey("cloudtrailsBucket").withParameterValue(getCloudTrailsBucket()),
                        Parameter().withParameterKey("BuddyAccountId").withParameterValue(getBuddyAccountId().toString()))

        val updateRequest = UpdateStackRequest()
                .withCapabilities(Capability.CAPABILITY_IAM, Capability.CAPABILITY_NAMED_IAM)
                .withStackName(stackName).withTemplateBody(templateBody)
                .withParameters(
                        Parameter().withParameterKey("cloudtrailsBucket").withParameterValue(getCloudTrailsBucket()),
                        Parameter().withParameterKey("BuddyAccountId").withParameterValue(getBuddyAccountId().toString()))
        createOrUpdateStack(client, createRequest, updateRequest)
    }
}

fun createOrUpdateStack(client: AmazonCloudFormation, createStackRequest: CreateStackRequest, updateStackRequest: UpdateStackRequest) {
    val existsAlready = client.listStacks().stackSummaries.any {
        it.stackName == createStackRequest.stackName && it.stackStatus != "DELETE_COMPLETE"

    }
    if (existsAlready) {
        try {
            val updateStackResult = client.updateStack(updateStackRequest)
            val waiter = client.waiters().stackUpdateComplete()
            waiter.run(WaiterParameters(DescribeStacksRequest().withStackName(updateStackRequest.stackName)))
            printStackStatus(client, updateStackRequest.stackName)

        } catch (any: AmazonCloudFormationException) {
            println("warning on ${updateStackRequest?.stackName}: ${any.message}")
        }
    } else {
        client.createStack(createStackRequest)
        val waiter = client.waiters().stackCreateComplete()
        waiter.run(WaiterParameters(DescribeStacksRequest().withStackName(createStackRequest.stackName)))
        printStackStatus(client, createStackRequest.stackName)
    }
}

fun printStackStatus(client: AmazonCloudFormation, stackName: String) {
    val stacks = client.describeStacks(DescribeStacksRequest().withStackName(stackName)).stacks

    for (stack: Stack in stacks) {
        println("${stack.stackName} is in status ${stack.stackStatus}")
    }
}

fun runLastCloudFormationInBuddyAccount() {
    val awsSecurityTokenService = AWSSecurityTokenServiceClientBuilder.standard().build()
    val assumeRoleResult = awsSecurityTokenService.assumeRole(AssumeRoleRequest()
            .withRoleArn("arn:aws:iam::${getBuddyAccountId()}:role/OrganizationAccountAccessRole").withRoleSessionName("CloudBuddyProvisioning")
            .withDurationSeconds(900))

    val credentialsProvider = AWSStaticCredentialsProvider(BasicSessionCredentials(assumeRoleResult.credentials.accessKeyId,
            assumeRoleResult.credentials.secretAccessKey,
            assumeRoleResult.credentials.sessionToken))

    val client: AmazonCloudFormation = AmazonCloudFormationClientBuilder.standard().withCredentials(credentialsProvider).build()

    "cloudformation-for-buddy-account-stack.json".asResource { templateBody ->
        val stackName = "cloud-buddy-for-buddy-account-dev"

        val createStackRequest = CreateStackRequest()
                .withCapabilities(Capability.CAPABILITY_IAM, Capability.CAPABILITY_NAMED_IAM)
                .withParameters(Parameter().withParameterKey("AccountIdParam").withParameterValue(getAccountId().toString()))
                .withParameters(Parameter().withParameterKey("BuddyEmailParam").withParameterValue(getBuddyEmail()))
                .withParameters(Parameter().withParameterKey("CloudTrailsBucketParam").withParameterValue(getCloudTrailsBucket()))
                .withStackName(stackName).withTemplateBody(templateBody)

        val updateStackRequest = UpdateStackRequest()
                .withCapabilities(Capability.CAPABILITY_IAM, Capability.CAPABILITY_NAMED_IAM)
                .withParameters(Parameter().withParameterKey("AccountIdParam").withParameterValue(getAccountId().toString()))
                .withParameters(Parameter().withParameterKey("BuddyEmailParam").withParameterValue(getBuddyEmail()))
                .withParameters(Parameter().withParameterKey("CloudTrailsBucketParam").withParameterValue(getCloudTrailsBucket()))
                .withStackName(stackName).withTemplateBody(templateBody)
        createOrUpdateStack(client, createStackRequest, updateStackRequest)
    }
}




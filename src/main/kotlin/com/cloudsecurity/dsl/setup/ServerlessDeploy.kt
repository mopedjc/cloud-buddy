package com.cloudsecurity.dsl.setup

import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.TimeoutRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.TimeUnit


fun main(args : Array<String>) {
    oneclickDeployServerless()
}

fun oneclickDeployServerless() {
    retryForFiveMinutes()
}

/**
 * Because deployServerless() was throwing a STSAssumeError after account creation, but when I ran it manually after
 * few minutes, it worked.
 */
fun retryForFiveMinutes() {
    // Retry up to Five minutes, at 15 second interval.
    val retryTemplate = RetryTemplate()
    val retryPolicy = TimeoutRetryPolicy()
    retryPolicy.timeout = 5 * 60 * 1000L
    val fixedBackOffPolicy = FixedBackOffPolicy()
    fixedBackOffPolicy.backOffPeriod = 15 * 1000L
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy)
    retryTemplate.setRetryPolicy(retryPolicy)
    retryTemplate.execute { _ ->
        try {
            deployServerless()
        } catch (any: Throwable) {
            throw any
        }
    }
}

fun deployServerless() {
    val awsSecurityTokenService = AWSSecurityTokenServiceClientBuilder.standard().build()
    val assumeRoleResult = awsSecurityTokenService.assumeRole(AssumeRoleRequest()
            .withRoleArn("arn:aws:iam::${getBuddyAccountId()}:role/OrganizationAccountAccessRole" ).withRoleSessionName("CloudBuddyProvisioning")
            .withDurationSeconds(900))

    "serverless deploy".runCommand(Paths.get(System.getProperty("user.dir")).toFile(), assumeRoleResult.credentials.accessKeyId, assumeRoleResult.credentials.secretAccessKey,assumeRoleResult.credentials.sessionToken)
}

fun String.runCommand(workingDir: File, accessKey: String, secretKey: String, sessionToken: String) {
    val process = ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)

    val environmentVariables = process.environment()
    environmentVariables.set("AWS_ACCESS_KEY_ID", accessKey)
    environmentVariables.set("AWS_SECRET_ACCESS_KEY", secretKey)
    environmentVariables.set("AWS_SESSION_TOKEN", sessionToken)

    process.start()
            .waitFor(60, TimeUnit.MINUTES)
}
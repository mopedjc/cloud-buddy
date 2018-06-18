package com.cloudsecurity.dsl.setup

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEventsClientBuilder
import com.amazonaws.services.cloudwatchevents.model.PutPermissionRequest
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.identitymanagement.model.CreateAccountAliasRequest
import com.amazonaws.services.organizations.model.*
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


fun main(args : Array<String>) {
    oneclickCreateBuddyAccount()
}

fun doesBuddyAccountExists() : Boolean {
    val configFilePath = Paths.get(configFile)
    if (Files.exists(configFilePath)) {
        val config: Config = loadConfigFromFile(Paths.get(configFile))
        val parameters = loadParametersFromFile(Paths.get(parametersFile))
        if (config.buddyEmail == parameters.buddyEmail) {
            val accounts = listAllAccounts()
            // TODO figure out this
//            return accounts.any {
//                it.email == config.buddyEmail && it.id == config.accountId.toString()
//            }

            return accounts.any {
                it.id == config.accountId.toString()
            }
        } else {
            return false
        }
    } else {
        return false
    }
}

fun listAllAccounts() : List<Account> {
    val ret = ArrayList<Account>()
    val client = com.amazonaws.services.organizations.AWSOrganizationsClientBuilder.standard().build()
    val result: ListAccountsResult = client.listAccounts(ListAccountsRequest())
    // TODO paginate to get all accounts
//    do {
//        ret.addAll(result.accounts)
//
//    } while (result.nextToken != null)
    return result.accounts
}

fun oneclickCreateBuddyAccount() {
    val parameters = loadParametersFromFile(Paths.get(parametersFile))

    if (doesBuddyAccountExists()) {
        println("Skipping creating account for ${parameters.buddyEmail} because it already exists.")
        return
    }
    val buddyAccountId = createAccount(parameters.buddyEmail)
    val cloudtrailsBucket = "${generateAccountAliasFromEmail(parameters.buddyEmail)}-cloudtrails"
    val failedAlertsBucket = "${generateAccountAliasFromEmail(parameters.buddyEmail)}-failed-alerts"
    val config = Config(buddyAccountId.toLong(), getAccountAliasFromAWS(), parameters.buddyEmail, cloudtrailsBucket,failedAlertsBucket, getAccountIdFromAWS().toLong())
    saveToFile(config)
}

fun putPermissionsOnEventBus(buddyAccountId: Long?) {
    val awsSecurityTokenService = AWSSecurityTokenServiceClientBuilder.standard().build()
    val assumeRoleResult = awsSecurityTokenService.assumeRole(AssumeRoleRequest()
            .withRoleArn("arn:aws:iam::${buddyAccountId}:role/${organizationAccessRole}" ).withRoleSessionName("CloudBuddyProvisioning")
            .withDurationSeconds(900))

    val credentialsProvider = AWSStaticCredentialsProvider(BasicSessionCredentials(assumeRoleResult.credentials.accessKeyId,
            assumeRoleResult.credentials.secretAccessKey,
            assumeRoleResult.credentials.sessionToken))

    val client = AmazonCloudWatchEventsClientBuilder.standard().withCredentials(credentialsProvider).build()
    client.putPermission(PutPermissionRequest().withAction("events:PutEvents").withPrincipal(getAccountId().toString()).withStatementId("AllowAccount${getAccountId()}ToPublishEvents"))
}

val parametersFile = "parameters.yml"
val configFile = "config.yml"

fun getAccountId(): Long {
    val config = loadConfigFromFile(Paths.get(configFile))
    return config.accountId!!
}
fun getCloudTrailsBucket(): String {
    val config = loadConfigFromFile(Paths.get(configFile))
    return config.cloudtrailsBucket
}
/**
 * BUDDY_ACCOUNT_ID in config.dev.yml
 */
fun getBuddyAccountId(): Long {
    val config = loadConfigFromFile(Paths.get(configFile))
    return config.buddyAccountId!!
}

fun getBuddyAccountName(): String {
    val config = loadConfigFromFile(Paths.get(configFile))
    return "${config.accountAlias}secops"
}


fun getBuddyEmail() : String {
    val config = loadConfigFromFile(Paths.get(configFile))
    return config.buddyEmail
}

fun loadParametersFromFile(path: Path): Parameters {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, Parameters::class.java)
    }
}

fun loadConfigFromFile(path: Path): Config {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, Config::class.java)
    }
}

fun saveToFile(config: Config) {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return Files.newBufferedWriter(Paths.get(configFile)).use {
        mapper.writeValue(it, config)
    }
}

val organizationAccessRole = "OrganizationAccountAccessRole"

fun generateAccountAliasFromEmail(email: String): String {
    return email.split("@").first().replace(".","-")
}
/**
 * Returns buddyAccountId
 */
fun createAccount(buddyEmail: String) : String {
    val buddyAccountName = generateAccountAliasFromEmail(buddyEmail)
    val client = com.amazonaws.services.organizations.AWSOrganizationsClientBuilder.standard().build()
    val result = client.createAccount(CreateAccountRequest()
            .withAccountName(buddyAccountName)
            .withEmail(buddyEmail)
            .withRoleName(organizationAccessRole))
    var finished = false
    var status: DescribeCreateAccountStatusResult? = null
    while (!finished) {
        status = client.describeCreateAccountStatus(DescribeCreateAccountStatusRequest().withCreateAccountRequestId(result.createAccountStatus.id))
        if (status.createAccountStatus.state == "SUCCEEDED") {
            finished = true
        }
        if (status.createAccountStatus.state == "FAILED") {
            println("Failed in creating account with ${buddyEmail}")
            System.exit(0);
        }
    }
    println("Finished, creating account ${status?.createAccountStatus?.accountName} with id ${status?.createAccountStatus?.accountId}")
    return status?.createAccountStatus?.accountId!!
}

fun attachAccountAlias(validAlias: String, buddyAccountId: Long?) {
    val awsSecurityTokenService = AWSSecurityTokenServiceClientBuilder.standard().build()
    val assumeRoleResult = awsSecurityTokenService.assumeRole(AssumeRoleRequest()
            .withRoleArn("arn:aws:iam::${buddyAccountId}:role/${organizationAccessRole}" ).withRoleSessionName("CloudBuddyProvisioning")
            .withDurationSeconds(900))

    val credentialsProvider = AWSStaticCredentialsProvider(BasicSessionCredentials(assumeRoleResult.credentials.accessKeyId,
            assumeRoleResult.credentials.secretAccessKey,
            assumeRoleResult.credentials.sessionToken))

    val existingAliases = AmazonIdentityManagementClientBuilder.standard().withCredentials(credentialsProvider).build().listAccountAliases()
    println("Existing Aliases : ${existingAliases.accountAliases}")
    AmazonIdentityManagementClientBuilder.standard().withCredentials(credentialsProvider).build().createAccountAlias(CreateAccountAliasRequest().withAccountAlias(validAlias))
}

fun listAccountAliases(buddyAccountId: String) :List<String> {

    val awsSecurityTokenService = AWSSecurityTokenServiceClientBuilder.standard().build()
    val assumeRoleResult = awsSecurityTokenService.assumeRole(AssumeRoleRequest()
            // used to be OrganizationAccountAccessRole, Prior June 5, 2018
            .withRoleArn("arn:aws:iam::${buddyAccountId}:role/${organizationAccessRole}" ).withRoleSessionName("CloudBuddyProvisioning")
            .withDurationSeconds(900))

    val credentialsProvider = AWSStaticCredentialsProvider(BasicSessionCredentials(assumeRoleResult.credentials.accessKeyId,
            assumeRoleResult.credentials.secretAccessKey,
            assumeRoleResult.credentials.sessionToken))

    val existingAliases = AmazonIdentityManagementClientBuilder.standard().withCredentials(credentialsProvider).build().listAccountAliases()
    println("Listing,.. Existing Aliases : ${existingAliases.accountAliases}")
    return AmazonIdentityManagementClientBuilder.standard().withCredentials(credentialsProvider).build().listAccountAliases().accountAliases
}
package com.secops.setup

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest
import java.nio.file.Paths


fun main(args : Array<String>) {
    val accountId = getAccountIdFromAWS()
    val config = loadConfigFromFile(Paths.get(configFile))
    config.accountId = accountId.toLong()
    config.accountAlias = getAccountAliasFromAWS()
    saveToFile(config)
    println("AccountId is ${accountId}")
    println("AccountAlias is ${config.accountAlias}")

}
// aws sts get-caller-identity --output text --query 'Account'
fun getAccountIdFromAWS() :String {
    val getCallerIdentityResult = AWSSecurityTokenServiceClientBuilder.standard().build().getCallerIdentity(GetCallerIdentityRequest())
    return getCallerIdentityResult.account
}

fun getAccountAliasFromAWS() :String {
    return AmazonIdentityManagementClientBuilder.standard().build().listAccountAliases().accountAliases.first()
}


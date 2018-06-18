package com.cloudsecurity.dsl.setup

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsyncClientBuilder
import java.nio.file.Paths


fun main(args : Array<String>) {
    if (isUsingRootCredentials()) {
        println("Don't use the root credentials.  Please set up a user which can assume a role")
        System.exit(0)
    }
    oneclickCreateBuddyAccount()
    oneclickDeployServerless()
    oneclickFinalTouchesToBuddyAccount()
    oneclickRunCloudFormation()
}

fun isUsingRootCredentials(): Boolean {
    val client = AmazonIdentityManagementAsyncClientBuilder.standard().build()
    val user =  client.user
    return user.user.arn.endsWith(":root")
}

/**
 * Must run before cloudformation
 */
fun oneclickFinalTouchesToBuddyAccount() {
    val config: Config = loadConfigFromFile(Paths.get(configFile))
    putPermissionsOnEventBus(config.buddyAccountId)
    val aliasExistAlready = listAccountAliases(config.buddyAccountId.toString()).any {
        it == generateAccountAliasFromEmail(config.buddyEmail)
    }
    if (!aliasExistAlready) {
        attachAccountAlias(generateAccountAliasFromEmail(config.buddyEmail), config.buddyAccountId)
    }
}


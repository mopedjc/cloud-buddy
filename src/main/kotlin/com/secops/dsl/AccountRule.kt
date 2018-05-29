package com.secops.dsl

data class AccountRule(var name: String? = null,
                       var description: String? = null,
                       var fail: Fail? = null) {
    operator fun invoke(body: AccountRule.() -> Unit) {
        body()
    }
}

fun AccountRule(block: AccountRule.() -> Unit): AccountRule = AccountRule().apply(block)
package com.cloudsecurity.dsl.attemptingdsl2

import com.cloudsecurity.dsl.attemptingdsl1.AccountRule
import com.cloudsecurity.dsl.attemptingdsl1.Fail

@DslMarker
annotation class dsl1

@dsl1
class FailBuilder(initialIsFail: () -> Boolean) {
    var isFail: () -> Boolean = initialIsFail

    fun build(): Fail {
        return Fail(isFail)
    }
}

@dsl1
class AccountRuleBuilder(initialName: String, initialDescription: String, initialFail: Fail) {
    var name: String = initialName
    var description: String = initialDescription
    var fail: Fail = initialFail

    fun build(): AccountRule {
        return AccountRule(name, description, fail)
    }

    fun fail(aIsFail: () -> Boolean = { -> true}, setup: FailBuilder.() -> Unit = {}) {
        val failBuilder = FailBuilder(aIsFail)
        failBuilder.setup()
        fail = failBuilder.build()
    }

}

inline fun accountRule(setup: AccountRuleBuilder.() -> Unit): AccountRule {
    val accountRuleBuilder = AccountRuleBuilder("dummyName", "dummyDescription", FailBuilder({-> true }).build())
    accountRuleBuilder.setup()
    return accountRuleBuilder.build()
}





package com.cloudsecurity.dsl.usingdsl

import com.cloudsecurity.dsl.attemptingdsl1.AccountRule
import com.cloudsecurity.dsl.attemptingdsl1.fail


class AlwaysFail {

    val rule = AccountRule {
        name = "AlwaysFail"
        description = "This rule always fails"

        fail = fail {
            isFail = { -> true }
        }
    }

    fun getName() : String? {
        return rule.name
    }

    fun isFail() : Boolean? {
        return rule.fail?.isFail?.invoke()
    }

}
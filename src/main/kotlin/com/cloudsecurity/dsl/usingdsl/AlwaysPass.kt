
package com.cloudsecurity.dsl.usingdsl

import com.cloudsecurity.dsl.attemptingdsl1.AccountRule
import com.cloudsecurity.dsl.attemptingdsl1.fail


class AlwaysPass {

    val rule = AccountRule {
        name = "AlwaysPass"
        description = "This rule always passes"

        fail = fail {
            isFail = { -> false }
        }
    }

    fun getName() : String? {
        return rule.name
    }

    fun isFail() : Boolean? {
        return rule.fail?.isFail?.invoke()
    }
}
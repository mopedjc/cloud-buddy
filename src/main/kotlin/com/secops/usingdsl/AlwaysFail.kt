
package com.secops.usingdsl

import com.secops.dsl.AccountRule
import com.secops.dsl.fail

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
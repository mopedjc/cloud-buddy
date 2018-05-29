
package com.secops.usingdsl

import com.secops.dsl.AccountRule
import com.secops.dsl.fail

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
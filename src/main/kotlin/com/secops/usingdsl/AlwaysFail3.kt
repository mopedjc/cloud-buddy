
package com.secops.usingdsl

import com.secops.dsl1.accountRule

class AlwaysFail3 {
    val rule = accountRule {
        name = "AlwaysFail3"
        description = "This rule always fails"

        fail {
            isFail = { -> true }
        }
    }

    fun getName(): String? {
        return rule.name
    }

    fun isFail(): Boolean? {
        return rule.fail?.isFail?.invoke()
    }
}
package com.secops.usingdsl

import com.secops.dsl1.accountRule

val rule = accountRule {
    name = "AlwaysFail2"
    description = "This rule always fails"

    fail {
        isFail = { -> true }
    }
}
package com.cloudsecurity.dsl.usingdsl

import com.cloudsecurity.dsl.attemptingdsl2.accountRule


val rule = accountRule {
    name = "AlwaysFail2"
    description = "This rule always fails"

    fail {
        isFail = { -> true }
    }
}
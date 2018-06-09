package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.FilterSNS
import org.jetbrains.annotations.NotNull

trait UserHasMFASNSFilter implements FilterSNS {

    @Override
    String getResource(@NotNull Map<String, ?> sns) {
        sns?.detail?.requestParameters.userName
    }

    @Override
    boolean isRelevant(String eventName) {
        eventName in [
                "EnableMFADevice",
                "DeactivateMFADevice",
                "CreateLoginProfile",

        ]
    }
}

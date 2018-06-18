package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.FilterSNS
import org.jetbrains.annotations.NotNull

trait ImagePublicSNSFilter implements FilterSNS {
    @Override
    String getResource(@NotNull Map<String, ?> sns) {
        sns?.detail?.requestParameters?.imageId
    }

    @Override
    boolean isRelevant(@NotNull String eventName) {
        eventName in ["ModifyImageAttribute"]
    }
}
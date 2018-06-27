package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.FilterSNS
import org.jetbrains.annotations.NotNull

class ImagePublicFilterSNS implements FilterSNS {
    @Override
    String getResource(@NotNull Map<String, ?> sns) {
        sns?.detail?.requestParameters?.imageId
    }

    @Override
    boolean isRelevant(@NotNull String eventName) {
        eventName in ["ModifyImageAttribute"]
    }
}
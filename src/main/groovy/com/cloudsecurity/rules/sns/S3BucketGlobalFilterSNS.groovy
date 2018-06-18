package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.FilterSNS
import org.jetbrains.annotations.NotNull

class S3BucketGlobalFilterSNS implements FilterSNS {

    @Override
    String getResource(@NotNull Map<String, ?> sns) {
        sns?.detail?.requestParameters?.bucketName
    }

    @Override
    boolean isRelevant(String eventName) {
        eventName in [
                "PutBucketPolicy",
                "DeleteBucketPolicy"
        ]
    }
}

package com.secops.setup

import com.fasterxml.jackson.annotation.JsonProperty

data class Config(@JsonProperty("BUDDY_ACCOUNT_ID") val buddyAccountId: String,
                  @JsonProperty("ACCOUNT_NAME") val accountName: String,
                  val buddyEmail: String,
                  @JsonProperty("s3FailedAlertsBucket") val s3FailedAlertsBucket: String,
                  @JsonProperty("ACCOUNT_ID") val accountId: String)
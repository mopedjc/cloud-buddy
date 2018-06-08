package com.secops.setup

import com.fasterxml.jackson.annotation.JsonProperty

data class Config(@JsonProperty("BUDDY_ACCOUNT_ID") var buddyAccountId: Long?,
                  @JsonProperty("ACCOUNT_ALIAS") var accountAlias: String?,
                  var buddyEmail: String,
                  val cloudtrailsBucket: String,
                  @JsonProperty("s3_failed_alerts_bucket") val s3FailedAlertsBucket: String,
                  @JsonProperty("ACCOUNT_ID") var accountId: Long?)
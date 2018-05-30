package com.secops.setup

import com.fasterxml.jackson.annotation.JsonProperty

data class Config(@JsonProperty("BUDDY_ACCOUNT_ID") val buddyAccountId: String,
                  @JsonProperty("ACCOUNT_NAME") val accountName: String,
                  val buddyEmail: String,
                  val s3_failed_alerts_bucket: String,
                  @JsonProperty("ACCOUNT_ID") val accountId: String,
                  val SIMULATION_CALLER_ARN: String)
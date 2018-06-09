package com.cloudsecurity.dsl.attemptingdsl1

class AwsService(var name: String? = null)

fun AwsService(block: AwsService.() -> Unit): AwsService = AwsService().apply(block)
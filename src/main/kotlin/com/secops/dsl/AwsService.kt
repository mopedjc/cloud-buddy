package com.secops.dsl

class AwsService(var name: String? = null)

fun AwsService(block: AwsService.() -> Unit): AwsService = AwsService().apply(block)
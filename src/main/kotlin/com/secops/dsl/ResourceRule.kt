package com.secops.dsl

data class ResourceRule(var name: String? = null)

fun ResourceRule(block: ResourceRule.() -> Unit): ResourceRule = ResourceRule().apply(block)
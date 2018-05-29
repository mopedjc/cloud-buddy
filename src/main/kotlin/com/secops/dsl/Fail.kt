package com.secops.dsl

typealias IsFailFunctionType = () -> Boolean

//data class Fail(var isFail: IsFailFunctionType? = null)
data class Fail(var isFail: ( () -> Boolean) = {-> true})


fun fail(block: Fail.() -> Unit): Fail = Fail().apply(block)
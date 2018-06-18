package com.cloudsecurity.dsl

interface UsingSDK {
    fun isFail(resource : String? = null) : Alert
    fun getName() :String
}
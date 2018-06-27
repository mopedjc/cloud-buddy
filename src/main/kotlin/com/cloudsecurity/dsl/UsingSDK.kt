package com.cloudsecurity.dsl

interface UsingSDK<ResourceType> {
    fun isFail(resource : ResourceType? = null) : Alert
    fun getName() :String
}
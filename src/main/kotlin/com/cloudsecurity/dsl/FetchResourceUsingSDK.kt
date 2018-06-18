package com.cloudsecurity.dsl

interface FetchResourceUsingSDK<ResourceType> {
    fun getResourceByName(resource : String) : ResourceType
}
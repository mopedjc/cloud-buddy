package com.cloudsecurity.dsl

interface FilterSNS {
    fun getResource(sns : Map<String, Any>) :String
    fun isRelevant(eventName: String): Boolean
}
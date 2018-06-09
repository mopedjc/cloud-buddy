package com.cloudsecurity.dsl

interface UsingSNS : FilterSNS {
    fun isFail(sns : String) : Alert
    fun getName() :String {
        return this.javaClass.name
    }
}
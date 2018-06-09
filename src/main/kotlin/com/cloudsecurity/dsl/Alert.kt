package com.cloudsecurity.dsl

import groovy.json.JsonOutput

data class Alert(
    var name: String? = null,
    var resource: String? = null,

    var message: String? = null,
    var fail: Boolean = false,

    var region: String? = null,

    var eventTime: String? = null,
    var eventName: String? = null,
    var details: MutableMap<String, Any> = mutableMapOf()
) {
    fun toJson() :String {

        return JsonOutput.toJson(
                mapOf("name" to name, "fail" to fail, "message" to message, "region" to region,
                        "resource" to resource, "eventTime" to eventTime, "eventName" to eventName,
                        "details" to details))
    }
}




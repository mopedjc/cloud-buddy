package com.secops.util

open class EnvironmentVariables {

    open fun getenv(variable: String): String {
        return System.getenv(variable)
    }
}


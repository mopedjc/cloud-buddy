package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import groovy.json.JsonSlurper

class VerifyPasswordPolicyChange : UsingSNS {

    override fun isFail(sns: String): Alert {
        val snsMap = snsStringToMap(sns)

        val snsDetail = snsMap["detail"] as Map<String,Any?>

        val policy = snsDetail["requestParameters"] as Map<String, Any?>
        val minimumPasswordLength = policy["minimumPasswordLength"] as Int
        val failedPassword = minimumPasswordLength < 8
        return Alert(fail = failedPassword)
    }


    override fun getResource(sns: Map<String, Any>): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRelevant(eventName: String): Boolean {
        return eventName == "UpdateAccountPasswordPolicy"
    }

    fun fillDetails(result: Alert?, sns: Map<String, Any>): Alert? {

       // result?.details?.put("whoDidIt",  (((sns["detail"] as Map<String, Any>)["userIdentity"] as Map<String, Any>)["userName"])
//        result?.details["whoDidItArn"] = sns?.detail?.userIdentity?.arn
//        result?.details["eventID"] = sns?.detail?.eventID
//        result?.details["eventType"] = sns?.detail?.eventType
//        result?.details["eventTime"] = sns?.detail?.eventTime
//        result?.details["sourceIPAddress"] = sns?.detail?.sourceIPAddress
//        result?.details["userAgent"] = sns?.detail?.userAgent
        return result
    }

    fun snsStringToMap(snsString: String?): MutableMap<Any?, Any?> {
        val slurper = JsonSlurper()
        val sns = slurper.parseText(snsString) as MutableMap<Any?, Any?>
        return sns
    }
}

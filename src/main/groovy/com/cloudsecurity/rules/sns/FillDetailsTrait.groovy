package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import groovy.json.JsonSlurper


trait FillDetailsTrait  {
	def fillDetails(Alert result, sns) {
		result.details['whoDidIt'] = sns?.detail?.userIdentity?.userName
		result.details['whoDidItArn'] = sns?.detail?.userIdentity?.arn
		result.details['eventID'] = sns?.detail?.eventID
		result.details['eventType'] = sns?.detail?.eventType
		result.details['eventName'] = sns?.detail?.eventName
		result.details['eventTime'] = sns?.detail?.eventTime
		result.details['sourceIPAddress'] = sns?.detail?.sourceIPAddress
		result.details['userAgent'] = sns?.detail?.userAgent
		result
	}

	Map snsStringToMap(String snsString) {
		JsonSlurper slurper = new JsonSlurper()
		Map<String, ?> sns = slurper.parseText(snsString)
		sns
	}
}

package com.cloudsecurity.rules

import groovy.json.JsonOutput
import groovy.transform.Canonical

@Canonical
class Result {
	String name
	String resource
	String message
	boolean fail
	String region

	String eventTime
	String eventName
	Map details = [:]

	String toJson() {
		JsonOutput.toJson([name: name, fail: fail, message: message, region: region, resource: resource,
						   eventTime: eventTime,
							eventName: eventName, details: details])
	}

	boolean isFail() {
		fail
	}
}

package com.cloudsecurity.dsl

import spock.lang.Specification

class AlertTest extends Specification {

	def "We can create an Alert, and get json"() {
		Alert myAlert = new Alert("simple", "TestResource", "TestMessage", true, "us-east-1", ["anotherItem": "123"])

		expect:
			myAlert.toJson() == """{"name":"simple","fail":true,"message":"TestMessage","region":"us-east-1","resource":"TestResource","details":{"anotherItem":"123"}}"""

	}

	def "can json details"() {

		Alert result = new Alert(details: [name: 'MyName'])

		expect:
			result.toJson() == """{"name":null,"fail":false,"message":null,"region":null,"resource":null,"details":{"name":"MyName"}}"""
	}
}

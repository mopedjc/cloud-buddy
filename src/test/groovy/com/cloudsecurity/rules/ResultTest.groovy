package com.cloudsecurity.rules

import spock.lang.Specification

class ResultTest extends Specification {

	def "can json details"() {

		Result result = new Result(details: [name: 'MyName'])

		expect:
			result.toJson() == """{"name":null,"fail":false,"message":null,"region":null,"resource":null,"eventTime":null,"eventName":null,"details":{"name":"MyName"}}"""
	}
}

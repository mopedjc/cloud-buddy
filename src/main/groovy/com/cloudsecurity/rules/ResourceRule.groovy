package com.cloudsecurity.rules

trait ResourceRule {

	boolean isResourceRule() {
		true
	}
	abstract boolean isRelevant(String eventName)
	abstract Result isFail(String resource, details)
	abstract String getName()
	abstract String getResource(details)
}

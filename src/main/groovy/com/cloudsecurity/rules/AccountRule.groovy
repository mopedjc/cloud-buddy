package com.cloudsecurity.rules

trait AccountRule {

	boolean isResourceRule() {
		false
	}
	abstract Result isFail()
	abstract String getName()
}

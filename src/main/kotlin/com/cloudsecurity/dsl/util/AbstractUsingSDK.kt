package com.cloudsecurity.dsl.util


abstract class AbstractUsingSDK(clientFactory: AWSClientFactory = STSAssumeRoleClientFactory) : AWSClientFactory by clientFactory

package com.cloudsecurity.rules

import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class SecurityGroupIngress implements ResourceRule {
	@Delegate AWSClientFactory clientFactory
	EnvironmentVariables environmentVariables = new EnvironmentVariables()

	SecurityGroupIngress(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
		this.clientFactory = clientFactory
	}

	@Override
	boolean isRelevant(String eventName) {
		eventName in ['AuthorizeSecurityGroupIngress']
	}

	@Override
	Result isFail(String resource, details = [:]) {
		def requestParameters = details?.requestParameters;
		def ipPermissions = requestParameters?.ipPermissions
		List ipPermissionsItems = ipPermissions?.items
		Result result = new Result(resource: resource, region: details?.awsRegion)
		boolean open22 = ipPermissionsItems.any {
			item ->
				def ipv6Items = item.ipv6Ranges.items
				def ipItems = item.ipRanges.items

				item.fromPort == 22 && item.toPort == 22 &&
						(ipv6Items.any { ipv6Item -> ipv6Item.cidrIpv6 == "::/0" } ||
								ipItems.any { ipItem -> ipItem.cidrIp == '0.0.0.0/0' })

		}
		result.fail = open22
		result
	}

	@Override
	String getName() {
		'SecurityGroupIngress22'
	}

	@Override
	String getResource(Object details) {
		details?.requestParameters?.groupId
	}
}

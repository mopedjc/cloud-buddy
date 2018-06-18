package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSNS
import org.jetbrains.annotations.NotNull

class SecurityGroupIngress implements UsingSNS, FillDetailsTrait {

	def ports = [22, 3306]

	@Override
	boolean isRelevant(String eventName) {
		eventName in ['AuthorizeSecurityGroupIngress']
	}

	@Override
	String getResource(@NotNull Map<String, ?> sns) {
		sns?.detail?.requestParameters?.groupId
	}

	@Override
	Alert isFail(@NotNull String snsString) {
		Map<String,?> sns = snsStringToMap(snsString)

		def snsDetails = sns.detail
		def resource = getResource(snsDetails)
		def eventName = snsDetails?.eventName
		def region = snsDetails?.awsRegion
		def requestParameters = snsDetails?.requestParameters;
		def ipPermissions = requestParameters?.ipPermissions
		List ipPermissionsItems = ipPermissions?.items
		Alert result = new Alert(resource: resource, eventName: eventName, region: region)
		boolean openOnPort = ipPermissionsItems.any {
			item ->
				def ipv6Items = item.ipv6Ranges.items
				def ipItems = item.ipRanges.items

				item.fromPort in ports && item.toPort in ports &&
						(ipv6Items.any { ipv6Item -> ipv6Item.cidrIpv6 == "::/0" } ||
								ipItems.any { ipItem -> ipItem.cidrIp == '0.0.0.0/0' })

		}
		result.fail = openOnPort
		fillDetails(result, sns)
	}

	@Override String getName() {
		return this.class.name
	}
}

package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.UsingSNS
import com.jayway.jsonpath.Filter
import com.jayway.jsonpath.JsonPath
import org.jetbrains.annotations.NotNull

import static com.jayway.jsonpath.Criteria.where
import static com.jayway.jsonpath.Filter.filter

class ImagePublic implements UsingSNS, FillDetailsTrait, ImagePublicSNSFilter {
	@Override com.cloudsecurity.dsl.Alert isFail(@NotNull String snsString) {
		String version = JsonPath.read(snsString, '$.detail.eventVersion' )
		def launchPermissions = JsonPath.read(snsString, '$.detail.requestParameters.launchPermission')

		boolean isPublic = launchPermissions.add && launchPermissions.add.items.any { it == ['group': 'all'] }

		String resource = JsonPath.read(snsString, '$.detail.requestParameters.imageId')
		String region = JsonPath.read(snsString, '$.detail.awsRegion')
		String eventName = JsonPath.read(snsString, '$.detail.eventName')
		String eventTime = JsonPath.read(snsString, '$.detail.eventTime')

		Map<String,?> sns = snsStringToMap(snsString)
		fillDetails(new com.cloudsecurity.dsl.Alert(name: name, resource: resource, message: '', fail: isPublic,region: region, eventName: eventName, eventTime: eventTime), sns)
	}

	Filter isAdd = filter( where('add').exists(true))
	Filter isRemove = filter( where('remove').exists(true))

	@Override
	String getName() {
		return this.class.name
	}
}

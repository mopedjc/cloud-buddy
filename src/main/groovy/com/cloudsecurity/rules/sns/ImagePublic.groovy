package com.cloudsecurity.rules.sns

import com.cloudsecurity.dsl.UsingSNS
import com.jayway.jsonpath.Filter
import com.jayway.jsonpath.JsonPath
import org.jetbrains.annotations.NotNull

import static com.jayway.jsonpath.Criteria.where
import static com.jayway.jsonpath.Filter.filter

class ImagePublic extends ImagePublicFilterSNS implements UsingSNS, FillDetailsTrait {
	String region = System.getenv('AWS_DEFAULT_REGION')


	@Override com.cloudsecurity.dsl.Alert isFail(@NotNull String snsString) {
		String version = JsonPath.read(snsString, '$.detail.eventVersion' )
		def launchPermissions = JsonPath.read(snsString, '$.detail.requestParameters.launchPermission')

		boolean isPublic = launchPermissions.add && launchPermissions.add.items.any { it == ['group': 'all'] }

		String resource = JsonPath.read(snsString, '$.detail.requestParameters.imageId')
		String region = JsonPath.read(snsString, '$.detail.awsRegion')

		Map<String,?> sns = snsStringToMap(snsString)
		fillDetails(new com.cloudsecurity.dsl.Alert(name: name, resource: resource, message: '', fail: isPublic,region: region), sns)
	}

	Filter isAdd = filter( where('add').exists(true))
	Filter isRemove = filter( where('remove').exists(true))

	@Override
	String getName() {
		return this.class.name
	}
}

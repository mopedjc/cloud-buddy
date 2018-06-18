package com.cloudsecurity.dsl.util

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.identitymanagement.model.ListMFADevicesRequest
import com.amazonaws.services.identitymanagement.model.ListMFADevicesResult

class CachingAmazonIdentityManagement(private val actualClient2: AmazonIdentityManagement) : AmazonIdentityManagement by actualClient2 {
	val actualClient: AmazonIdentityManagement
	val cache = hashMapOf<Any, Any>()

	init {
		actualClient = actualClient2
	}

	override fun listMFADevices(request: ListMFADevicesRequest)  : ListMFADevicesResult =
			cache.getOrPut(request, {
				actualClient.listMFADevices(request)
			}) as ListMFADevicesResult


}

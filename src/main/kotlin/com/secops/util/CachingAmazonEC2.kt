package com.secops.util

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.DescribeImagesResult

class CachingAmazonEC2(private val actualClient2: AmazonEC2) : AmazonEC2 by actualClient2 {
	val actualClient: AmazonEC2
	val cache = hashMapOf<Any, Any>()

	init {
		actualClient = actualClient2
	}

	override fun describeImages(request: DescribeImagesRequest)  : DescribeImagesResult =
			cache.getOrPut(request, {
				actualClient.describeImages(request)
			}) as DescribeImagesResult
}

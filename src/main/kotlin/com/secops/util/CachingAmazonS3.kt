package com.secops.util

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.BucketPolicy
import com.amazonaws.services.s3.model.GetBucketLocationRequest
import com.amazonaws.services.s3.model.GetBucketPolicyRequest

class CachingAmazonS3(private val actualClient2: AmazonS3) : AmazonS3 by actualClient2 {
	val actualClient: AmazonS3
	val cache = hashMapOf<Any, Any>()

	init {
		actualClient = actualClient2
	}

	override fun getBucketLocation(bucket: String)  : String =
			cache.getOrPut(bucket, {
				actualClient.getBucketLocation(bucket)
			}) as String

	override fun getBucketLocation(bucket: GetBucketLocationRequest)  : String =
			cache.getOrPut(bucket, {
				actualClient.getBucketLocation(bucket)
			}) as String

	override fun getBucketPolicy(bucket: GetBucketPolicyRequest)  : BucketPolicy =
			cache.getOrPut(bucket, {
				actualClient.getBucketPolicy(bucket)
			}) as BucketPolicy


}

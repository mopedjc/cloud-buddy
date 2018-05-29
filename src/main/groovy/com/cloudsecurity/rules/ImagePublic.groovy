package com.cloudsecurity.rules

import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.Image
import com.cloudsecurity.STSAssumeRoleClientFactory
import com.secops.util.AWSClientFactory
import com.secops.util.EnvironmentVariables

class ImagePublic implements ResourceRule {
	@Delegate AWSClientFactory clientFactory
	EnvironmentVariables environmentVariables = new EnvironmentVariables()

	ImagePublic(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
		this.clientFactory = clientFactory
	}

	@Override
	boolean isRelevant(String eventName) {
		eventName in ["ModifyImageAttribute"]
	}

	@Override
	Result isFail(String resource, details = [:]) {
		Image image = ec2Client.describeImages(new DescribeImagesRequest(imageIds: [resource])).images.first()

		Result result = new Result(name: name, resource: resource, region: 'us-east-1', details: [name: image.name, creationDate: image.creationDate])
		result.fail = image.isPublic()
		if (image.isPublic()) {
			result.message = "${resource} image is public"
		} else {
			result.message = "${resource} image is private"
		}
		result
	}

	@Override
	String getName() {
		return 'ImagePublic'
	}

	@Override
	String getResource(Object details) {
		details?.requestParameters?.imageId
	}

}

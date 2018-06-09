package com.cloudsecurity.rules.sdk

import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.Image
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AWSClientFactory
import com.cloudsecurity.dsl.util.EnvironmentVariables
import com.cloudsecurity.rules.sns.ImagePublicSNSFilter

class ImagePublic implements UsingSDK, ImagePublicSNSFilter {
	@Delegate AWSClientFactory clientFactory
	EnvironmentVariables environmentVariables = new EnvironmentVariables()

	ImagePublic(AWSClientFactory clientFactory = STSAssumeRoleClientFactory.instance) {
		this.clientFactory = clientFactory
	}

	@Override
	Alert isFail(String resource) {
		Image image = ec2Client.describeImages(new DescribeImagesRequest(imageIds: [resource])).images.first()
		String region = environmentVariables.getenv("AWS_DEFAULT_REGION")
		Alert result = new Alert(name: name, resource: resource, region: region, details: [name: image.name, creationDate: image.creationDate])
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
		return this.class.name
	}

}

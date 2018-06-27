package com.cloudsecurity.rules.sdk

import com.amazonaws.services.ec2.model.Image
import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.dsl.UsingSDK
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class ImagePublic extends AbstractUsingSDK implements UsingSDK<Image> {
	String region = System.getenv('AWS_DEFAULT_REGION')

	@Override
	Alert isFail(Image image) {
		String region = environmentVariables.getenv("AWS_DEFAULT_REGION")
		String resource = image.imageId
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

package com.cloudsecurity.resources

import com.amazonaws.services.ec2.model.Image
import com.cloudsecurity.dsl.Alert
import spock.lang.Specification

class ListAllImagesTest extends Specification {

	def 'should be able to add details'() {
		ListAllImages listAllImages = new ListAllImages()
		Alert alert = new Alert(region: 'TestRegion', name: 'ImagePublic', resource: 'TestImageId', details: [:])
		Image image = Mock()
		image.name >> 'TestName'
		image.description >> 'TestDescription'
		image.creationDate >> 'TestCreateDate'
		image.imageId >> 'TestImageId'
		image.public >> true

		expect:
			alert.withDetails(listAllImages.details(image)) ==
					new Alert(region: 'TestRegion', name: 'ImagePublic', resource: 'TestImageId', details: [creationDate: 'TestCreateDate', description: 'TestDescription', imageId: 'TestImageId', isPublic: true, architecture: null, enaSupport:null, hypervisor:null, stateReason: null, kernelId: null, ownerId: null, platform: null, state: null, imageLocation: null, imageOwnerAlias: null, imageType: null])

	}

}

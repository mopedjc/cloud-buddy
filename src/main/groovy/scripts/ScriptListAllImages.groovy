package scripts

import com.cloudsecurity.dsl.Alert
import com.cloudsecurity.resources.ListAllImages
import com.cloudsecurity.rules.sdk.ImagePublic

ImagePublic imagePublic = new ImagePublic(clientFactory: LocalAWSClientFactory.instance)
imagePublic.region = 'test-region'

ListAllImages listAllImages = new ListAllImages(clientFactory: LocalAWSClientFactory.instance)
List<Alert> alertList = listAllImages.allResources().collect {
	imagePublic.isFail(it)
}

println alertList


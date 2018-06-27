package com.cloudsecurity.resources

import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.Image
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class ListAllImages extends AbstractUsingSDK {
  String region = System.getenv('AWS_DEFAULT_REGION')

  List<Image> allResources() {
    ec2Client.describeImages(new DescribeImagesRequest(owners: ['self']))?.images
  }

  def details(Image image) {
    [architecture: image.getArchitecture(),
     creationDate: image.getCreationDate(),
     description: image.getDescription(),
     enaSupport: image.getEnaSupport(),
     hypervisor: image.getHypervisor(),
     imageId: image.getImageId(),
     imageLocation: image.getImageLocation(),
     imageOwnerAlias: image.getImageOwnerAlias(),
     imageType: image.getImageType(),
            kernelId: image.getKernelId(),
            ownerId: image.getOwnerId(),
            platform: image.getPlatform(),
            state: image.getState(),
            isPublic: image.getPublic(),
            stateReason: image.getStateReason()]
  }


}

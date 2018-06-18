package com.cloudsecurity.resources

import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.Image
import com.cloudsecurity.dsl.FetchResourceUsingSDK
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class GetImage extends AbstractUsingSDK implements FetchResourceUsingSDK<Image> {

    @Override
    Image getResourceByName(String resource) {
        ec2Client.describeImages(new DescribeImagesRequest(imageIds: [resource])).images.first()
    }
}

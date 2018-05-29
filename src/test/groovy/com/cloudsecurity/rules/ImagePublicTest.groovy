package com.cloudsecurity.rules

import spock.lang.Specification

class ImagePublicTest extends Specification {


	def sns = """{
eventVersion: "1.05",
userIdentity: {
type: "IAMUser",
principalId: "AIDAI43GIBMCOUZRFCU7U",
arn: "arn:aws:iam::864374811737:user/johndoe",
accountId: "864374811737",
accessKeyId: "ASIAJWYXKBDDISOWL7JQ",
userName: "johndoe",
sessionContext: {
attributes: {
mfaAuthenticated: "false",
creationDate: "2018-05-13T04:36:04Z"
}
},
invokedBy: "signin.amazonaws.com"
},
eventTime: "2018-05-13T04:39:15Z",
eventSource: "ec2.amazonaws.com",
eventName: "CreateImage",
awsRegion: "us-east-1",
sourceIPAddress: "68.184.181.195",
userAgent: "signin.amazonaws.com",
requestParameters: {
instanceId: "i-0015dadb929e0e246",
name: "image1",
noReboot: false,
blockDeviceMapping: {
items: [
{
deviceName: "/dev/xvda",
ebs: {
volumeSize: 8,
deleteOnTermination: true,
volumeType: "gp2",
encrypted: false
}
}
]
}
},
responseElements: {
imageId: "ami-0fb26c653d4a13eba"
},
requestID: "9d8d6706-da6d-46f0-b7f8-9a9c271b90f8",
eventID: "c883028c-9420-4b8a-9bb7-aaade3520bb5",
eventType: "AwsApiCall",
recipientAccountId: "864374811737"
}"""
}



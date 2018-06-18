package scripts

import com.cloudsecurity.rules.sdk.S3BucketGlobalPut

S3BucketGlobalPut s3BucketGlobalPut = new S3BucketGlobalPut(clientFactory: LocalAWSClientFactory.instance)
s3BucketGlobalPut.region = 'test-region'

println s3BucketGlobalPut.isFail("jcazure2-dummy-bucket52")
package scripts

import com.cloudsecurity.rules.sdk.S3BucketGlobalGet

S3BucketGlobalGet s3BucketGlobalGet = new S3BucketGlobalGet(clientFactory: LocalAWSClientFactory.instance)
s3BucketGlobalGet.buddyAccountId = '123'

println s3BucketGlobalGet.isFail("jcazure2-dummy-bucket52")
package scripts

import com.cloudsecurity.rules.sdk.S3BucketGlobalDelete

S3BucketGlobalDelete s3BucketGlobalDelete = new S3BucketGlobalDelete(clientFactory: LocalAWSClientFactory.instance)
s3BucketGlobalDelete.buddyAccountId = '123'

println s3BucketGlobalDelete.isFail("jcazure2-dummy-bucket52")
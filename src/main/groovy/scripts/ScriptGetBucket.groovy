package scripts

import com.cloudsecurity.rules.S3BucketGlobalGet

S3BucketGlobalGet s3BucketGlobalGet = new S3BucketGlobalGet(LocalAWSClientFactory.instance)
s3BucketGlobalGet.environmentVariables = new LocalEnvironmentVariables()

println s3BucketGlobalGet.isFail("jcazure2-dummy-bucket52")
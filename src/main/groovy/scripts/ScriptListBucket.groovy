package scripts

import com.cloudsecurity.rules.sdk.S3BucketGlobalList

S3BucketGlobalList s3BucketGlobalList = new S3BucketGlobalList(LocalAWSClientFactory.instance)
s3BucketGlobalList.environmentVariables = new LocalEnvironmentVariables()

println s3BucketGlobalList.isFail("jcazure2-dummy-bucket52")
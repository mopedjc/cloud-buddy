package scripts

import com.cloudsecurity.rules.S3BucketGlobalDelete

S3BucketGlobalDelete s3BucketGlobalDelete = new S3BucketGlobalDelete(LocalAWSClientFactory.instance)
s3BucketGlobalDelete.environmentVariables = new LocalEnvironmentVariables()

println s3BucketGlobalDelete.isFail("jcazure2-dummy-bucket52")
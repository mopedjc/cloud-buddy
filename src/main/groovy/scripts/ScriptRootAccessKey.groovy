package scripts

import com.cloudsecurity.rules.sdk.RootAccessKey

RootAccessKey rootAccessKey = new RootAccessKey(LocalAWSClientFactory.instance)
rootAccessKey.environmentVariables = new LocalEnvironmentVariables()

println rootAccessKey.isFail()
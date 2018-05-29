package scripts

import com.cloudsecurity.rules.RootAccessKey

RootAccessKey rootAccessKey = new RootAccessKey(LocalAWSClientFactory.instance)
rootAccessKey.environmentVariables = new LocalEnvironmentVariables()

println rootAccessKey.isFail()
package scripts

import com.cloudsecurity.rules.sdk.RootAccessKey

RootAccessKey rootAccessKey = new RootAccessKey(clientFactory: LocalAWSClientFactory.instance)
rootAccessKey.region = 'test-region'

println rootAccessKey.isFail()
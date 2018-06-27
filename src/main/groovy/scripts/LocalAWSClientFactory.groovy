package scripts

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.cloudsecurity.dsl.util.AWSClientFactory
import com.cloudsecurity.dsl.util.AWSClientFactoryImpl

@Singleton
class LocalAWSClientFactory extends AWSClientFactoryImpl {

	AWSCredentialsProvider getCredentialsProvider() {
		new ProfileCredentialsProvider('johndoe_jcazure2')
	}
}

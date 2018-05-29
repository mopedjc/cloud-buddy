package scripts

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.secops.util.AWSClientFactory

@Singleton
class LocalAWSClientFactory extends AWSClientFactory {

	AWSCredentialsProvider getCredentialsProvider() {
		new ProfileCredentialsProvider('jcazure2')
	}
}

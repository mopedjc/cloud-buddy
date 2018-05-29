package scripts

import com.secops.util.EnvironmentVariables


class LocalEnvironmentVariables extends EnvironmentVariables {
	def env = ['SIMULATION_CALLER_ARN': 'arn:aws:iam::406431865824:user/maryjane']

	@Override
	String getenv(String var) {
		env[var]
	}
}

package scripts

import com.secops.util.EnvironmentVariables


class LocalEnvironmentVariables extends EnvironmentVariables {
	def env = ['ACCOUNT_ID': '123']

	@Override
	String getenv(String var) {
		env[var]
	}
}

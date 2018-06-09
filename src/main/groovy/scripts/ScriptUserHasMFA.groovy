package scripts

import com.cloudsecurity.rules.sdk.UserHasMFASDKRule

UserHasMFASDKRule hasMFA = new UserHasMFASDKRule(LocalAWSClientFactory.instance)
hasMFA.environmentVariables = new LocalEnvironmentVariables()

println hasMFA.isFail("johndoe")
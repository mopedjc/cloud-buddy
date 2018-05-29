package scripts

import com.cloudsecurity.rules.UserHasMFA

UserHasMFA hasMFA = new UserHasMFA(LocalAWSClientFactory.instance)
hasMFA.environmentVariables = new LocalEnvironmentVariables()

println hasMFA.isFail("johndoe")
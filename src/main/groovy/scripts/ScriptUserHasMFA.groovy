package scripts

import com.cloudsecurity.rules.sdk.UserHasMFA

UserHasMFA hasMFA = new UserHasMFA(clientFactory: LocalAWSClientFactory.instance)
hasMFA.region = 'test-region'

println hasMFA.isFail("johndoe")
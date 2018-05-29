package scripts

def iam = LocalAWSClientFactory.instance.identityManagementClient

println iam.listUsers().users



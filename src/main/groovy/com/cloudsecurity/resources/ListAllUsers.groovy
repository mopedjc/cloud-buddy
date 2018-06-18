package com.cloudsecurity.resources

import com.amazonaws.services.identitymanagement.model.User
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class ListAllUsers extends AbstractUsingSDK {
  String region = System.getenv('AWS_DEFAULT_REGION')

  List<User> allResources() {
    identityManagementClient.listUsers().users
  }

  def details(User user) {
    [createDate: user.createDate,
     path: user.path,
     userName: user.userName,
     userId: user.userId,
     arn: user.arn,
     passwordLastUsed: user.passwordLastUsed]
  }


}

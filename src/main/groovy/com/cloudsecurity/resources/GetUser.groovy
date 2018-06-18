package com.cloudsecurity.resources

import com.amazonaws.services.identitymanagement.model.ListUsersRequest
import com.amazonaws.services.identitymanagement.model.User
import com.cloudsecurity.dsl.FetchResourceUsingSDK
import com.cloudsecurity.dsl.util.AbstractUsingSDK

class GetUser extends AbstractUsingSDK implements FetchResourceUsingSDK<User> {

    @Override
    User getResourceByName(String resource) {
        identityManagementClient.listUsers(new ListUsersRequest()).users.find { it.userId == resource }
    }
}

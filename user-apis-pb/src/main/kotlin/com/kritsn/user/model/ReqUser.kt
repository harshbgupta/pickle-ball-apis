package com.kritsn.user.model

import com.kritsn.user.dto.User

data class ReqUser(
    var firstName: String? = null,
    var lastName: String? = null,
    var mobileNumber: String = "",
    var email: String? = null,
    var userType: String? = null
) {
    constructor(user: User) : this(
        firstName = user.firstName,
        lastName = user.lastName,
        mobileNumber = user.mobileNumber,
        email = user.email,
        userType = user.userType
    )
}
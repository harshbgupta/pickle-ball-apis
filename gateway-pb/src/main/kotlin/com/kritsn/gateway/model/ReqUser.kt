package com.kritsn.gateway.model

data class ReqUser (
    var firstName: String? = null,
    var lastName: String? = null,
    var mobileNumber: String = "",
    var email: String? = null,
    var userType: String? = null
)
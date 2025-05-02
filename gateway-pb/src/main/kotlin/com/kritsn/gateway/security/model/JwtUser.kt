package com.kritsn.gateway.security.model

data class JwtUser(
    var id: Long? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var userType: String? = null
)
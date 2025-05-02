package com.kritsn.gateway.security

import com.kritsn.gateway.security.model.JwtAuthenticationToken
import com.kritsn.gateway.security.model.JwtUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

/**
 * Copyright © 2021 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since January 16, 2021
 */
@Component
class JwtAuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {
    @Autowired
    private val validator: com.kritsn.gateway.security.JwtValidator? = null

    @Throws(AuthenticationException::class)
    override fun additionalAuthenticationChecks(
        userDetails: UserDetails,
        usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken
    ) {
    }

    @Throws(AuthenticationException::class)
    override fun retrieveUser(
        s: String,
        usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken
    ): UserDetails {
        val jwtAuthenticationToken = usernamePasswordAuthenticationToken as JwtAuthenticationToken
        val token = jwtAuthenticationToken.token

        val jwtUser = validator!!.validate(token) ?: throw IllegalAccessException("Token is incorrect")

        val grantedAuthorities = AuthorityUtils
            .commaSeparatedStringToAuthorityList(jwtUser.userType)
        return JwtUserDetails(jwtUser.phoneNumber!!, jwtUser.id!!, token, grantedAuthorities)
    }

    override fun supports(aClass: Class<*>): Boolean {
        return (JwtAuthenticationToken::class.java.isAssignableFrom(aClass))
    }
}

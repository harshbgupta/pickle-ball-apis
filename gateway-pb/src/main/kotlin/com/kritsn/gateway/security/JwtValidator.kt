package com.kritsn.gateway.security

import com.kritsn.gateway.security.model.JwtUser
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import com.kritsn.gateway.security.model.*

/**
 * Copyright © 2021 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since January 16, 2021
 */
@Component
class JwtValidator {
    fun validate(token: String?): JwtUser? {
        var jwtUser: JwtUser? = null
        try {
            val body = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .body

            jwtUser = JwtUser()

            jwtUser.phoneNumber = body.subject
            jwtUser.id = (body[JWT_KEY_ID] as String?)!!.toLong()
            jwtUser.userType = body[JWT_KEY_USER_TYPE] as String?
        } catch (e: Exception) {
            println(e)
        }

        return jwtUser
    }
}
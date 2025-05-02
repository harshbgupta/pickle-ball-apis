package com.kritsn.gateway.security.model

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken


/**
 * Copyright © 2021 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since January 16, 2021
 */
class JwtAuthenticationToken(var token: String) : UsernamePasswordAuthenticationToken(null, null) {
    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }
}

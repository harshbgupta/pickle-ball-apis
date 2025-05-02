package com.kritsn.gateway.security

import com.kritsn.gateway.security.model.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

/**
 * Copyright © 2021 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since January 16, 2021
 */

@Component
class JwtGenerator {

    fun generate(phoneNumber: String, userType: String?): String {
        val claims = Jwts.claims().setSubject(phoneNumber)
        claims[JWT_KEY_ID] = System.currentTimeMillis()
        claims[JWT_KEY_USER_TYPE] = userType
        return generateNewToken(claims)
    }

    fun refreshToken(oldToken: String): String? {
        return try {
            // Extract claims from the token (ignoring expiration)
            val claims = extractClaims(oldToken)
            if (!validateClaims(claims)) return null
            generateNewToken(claims)
        } catch (e: io.jsonwebtoken.ExpiredJwtException) {
            // Extract claims from the expired token
            val claims = e.claims
            if (!validateClaims(claims)) return null
            generateNewToken(claims)
        } catch (e: Exception) {
            println("Error refreshing token: ${e.message}")
            null
        }
    }

    private fun extractClaims(token: String): Map<String, Any> {
        return Jwts.parser()
            .setSigningKey(Base64.getDecoder().decode(JWT_SECRET_KEY))
            .parseClaimsJws(token)
            .body
    }

    private fun validateClaims(claims: Map<String, Any>): Boolean {
        return claims[JWT_ISSUER_KEY] == JWT_ISSUER_VALUE
    }

    private fun generateNewToken(claims: Map<String, Any>): String {

        return Jwts.builder()
            .setClaims(claims)
            .setHeaderParam(JWT_HEADER_PARAM_1_KEY, JWT_HEADER_PARAM_1_VALUE)
            .setHeaderParam(JWT_HEADER_PARAM_2_KEY, JWT_HEADER_PARAM_2_VALUE)
            .setIssuer(JWT_ISSUER_VALUE)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + JWT_EXPIRATION_TIME_MILLIS))
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
            .compact()
    }
}

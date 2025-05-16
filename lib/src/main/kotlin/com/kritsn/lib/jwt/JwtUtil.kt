package com.kritsn.lib.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

/**
 * Copyright © 2025 Kritsn LLP. All rights reserved.
 *
 * @author Radhey (hr-sh)
 * @since May 12, 2025
 */

@Component
class JwtUtil {
    private val secretKey =
        "d2dca4b9760f23b7d46579d53786123d6c48c77c971fb0f5da24158106ac22d73d4ec0676312ce8ad0408d7f57c663bb522685835e93bbb4733d68db1a459282c7a22b00124499349d4b053d63df07530fc8b5ebfaec2d143f06accf9f4489ababdb64588881db3d69c369678422168e1669cb306e6ee5b30893cb5bc2ecbe02a85565e8af3bcdb9e2ea5f475d830e6c5133e76ad8e884bf3fcfb6828d3f4093d8fc24aeae6a1caef506e865a9eae262e74a98f170d32114587574185551d05d67b703ae0a034fdc71f09bdcf7da84dc0003a1134024d1b739b488e478381acf4411f423c0d4ae244a8dd7ce77981fc2ca525c3f68a09f2d5ea012e99dae33ab"
    private val expirationTime = 1000 * 60 * 60 // 1 hour

    @Throws(Exception::class)
    fun generateToken(mobileNumber: String): String {
        val claims: Map<String, Any> = mapOf(MOBILE_NUMBER to mobileNumber)
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
//            .setIssuer("Kritsn LLP")
            //            .setIssuedAt(now)
            //            .setExpiration(Date(now.time + expirationTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    @Throws(Exception::class)
    fun validateToken(tokenWithBearer: String?): Boolean {
        tokenWithBearer ?: throw JwtException("Token is Null")
        val token = if (tokenWithBearer.startsWith(BEARER)) {
            tokenWithBearer.removePrefix(BEARER)
        } else {
            throw JwtException("Token must start with appropriate \"key\"")
        }
        val claims = getClaimsFromToken(token)
        return claims.expiration.after(Date())
    }

    @Throws(Exception::class)
    fun refreshToken(tokenWithBearer: String?): String {
        tokenWithBearer ?: throw JwtException("Token is Null")

        //Validating Token
        if (!validateToken(tokenWithBearer)) throw JwtException("Token is not valid")

        val jwtToken = if (tokenWithBearer.startsWith(BEARER)) {
            tokenWithBearer.removePrefix(BEARER)
        } else {
            throw JwtException("Token starting \"key\" is incorrect")
        }

        val claims = getClaimsFromToken(jwtToken)

        //Refreshing Token
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    }

    @Throws(Exception::class)
    fun getClaimsFromToken(tokenWithBearer: String): Claims {
        //Validating Token
        if (!validateToken(tokenWithBearer)) throw JwtException("Token is not valid")

        val jwtToken = if (tokenWithBearer.startsWith(BEARER)) {
            tokenWithBearer.removePrefix(BEARER)
        } else {
            throw JwtException("Token starting \"key\" is incorrect")
        }
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(jwtToken)
            .body

    }
}
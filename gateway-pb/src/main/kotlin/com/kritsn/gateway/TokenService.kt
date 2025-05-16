package com.kritsn.gateway

import com.kritsn.lib.base.Response
import com.kritsn.lib.base.buildErrorResponse
import com.kritsn.lib.base.buildSuccessResponse
import com.kritsn.lib.jwt.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Copyright © 2025 Kritsn LLP. All rights reserved.
 *
 * @author Radhey (hr-sh)
 * @since May 16, 2025
 */
@Service
class TokenService {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    fun handleGenerateToken(mobileNumber: String): Response<String> {
        try {
            val jwtToken = jwtUtil.generateToken(mobileNumber)
            return buildSuccessResponse(jwtToken)
        } catch (e: Exception) {
            e.printStackTrace()
            return buildErrorResponse(e.message)
        }
    }

    fun handleRefreshToken(tokenWithBearer: String?): Response<String> {
        try {
            //Refreshing token
            val refreshedJwtToken = jwtUtil.refreshToken(tokenWithBearer)
            return buildSuccessResponse(refreshedJwtToken)
        } catch (e: Exception) {
            e.printStackTrace()
            return buildErrorResponse(e.message)
        }
    }
}
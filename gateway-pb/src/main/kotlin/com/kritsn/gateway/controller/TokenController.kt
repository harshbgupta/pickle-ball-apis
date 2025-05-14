package com.kritsn.gateway.controller

import com.kritsn.gateway.model.ReqUser
import com.kritsn.lib.BEARER
import com.kritsn.lib.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Copyright © 2025 Kritsn LLP. All rights reserved.
 *
 * @author Radhey (hr-sh)
 * @since May 12, 2025
 */
@RestController
@RequestMapping("/token")
class TokenController {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @GetMapping("/dummy")
    private fun dummyApi(): String {
        return "this is a dummy api"
    }

    @PostMapping("/create")
    private fun createNewToken(@RequestPart reqUser: ReqUser): String {
        return jwtUtil.generateToken(reqUser.mobileNumber)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestHeader("Authorization") token: String): Map<String, String> {
        val actualToken = token.substringAfter(BEARER)
        if (!jwtUtil.validateToken(actualToken)) {
            throw IllegalArgumentException("Invalid or expired token")
        }
        val newToken = jwtUtil.refreshToken(actualToken)
        return mapOf("token" to newToken)
    }
}
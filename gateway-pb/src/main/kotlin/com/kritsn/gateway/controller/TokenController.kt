package com.kritsn.gateway.controller

import com.kritsn.gateway.TokenService
import com.kritsn.gateway.model.ReqUser
import com.kritsn.lib.base.Response
import com.kritsn.lib.base.buildSuccessResponse
import com.kritsn.lib.jwt.JwtUtil
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
    lateinit var tokenService: TokenService

    @GetMapping("/dummy")
    private fun dummyApi(): Response<String> {
        return buildSuccessResponse()
    }

    @PostMapping("/create", consumes = ["application/json"], produces = ["application/json"])
    private fun createNewToken(@RequestBody reqUser: ReqUser): Response<String> {
        return tokenService.handleGenerateToken(reqUser.mobileNumber)
    }

    @GetMapping("/refresh")
    fun refreshToken(@RequestHeader("Authorization") token: String?): Response<String> {
        return tokenService.handleRefreshToken(token)
    }
}
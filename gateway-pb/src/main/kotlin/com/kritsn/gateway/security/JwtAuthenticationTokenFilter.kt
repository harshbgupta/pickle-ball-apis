package com.kritsn.gateway.security

import com.kritsn.gateway.security.model.JwtAuthenticationToken
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.io.IOException

/**
 * Copyright © 2021 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since January 16, 2021
 */
class JwtAuthenticationTokenFilter : AbstractAuthenticationProcessingFilter("/rest/**") {
    @Throws(
        AuthenticationException::class,
        IOException::class,
        ServletException::class
    )
    override fun attemptAuthentication(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): Authentication {
        val header = httpServletRequest.getHeader("Authorization")


        if (header == null || !header.startsWith("AUTH ")) {
            throw IllegalAccessException("Token is missing")
        }

        val authenticationToken = header.substring(5)

        val token = JwtAuthenticationToken(authenticationToken)
        return authenticationManager.authenticate(token)
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        super.successfulAuthentication(request, response, chain, authResult)
        try {
            chain.doFilter(request, response)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ServletException) {
            e.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

package com.kritsn.gateway.security

import com.kritsn.lib.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Copyright © 2025 Kritsn LLP. All rights reserved.
 *
 * @author Radhey (hr-sh)
 * @since May 12, 2025
 */
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
class JwtAuthenticationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {
    private val excludedPaths = listOf(
        "$URL_PREFIX_PUBLIC/**",
        "$URL_PREFIX_OPEN/**",
        "$URL_PREFIX_TOKEN/**",
    )

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val requestPath = request.requestURI
        return excludedPaths.any { path -> requestPath.startsWith(path) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI
        if (requestURI.startsWith(URL_PREFIX_OPEN)
            || requestURI.startsWith(URL_PREFIX_PUBLIC)
            || requestURI.startsWith(URL_PREFIX_TOKEN)
        ) {
            filterChain.doFilter(request, response)
            return
        }
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header")
            return
        }
        val token = authHeader.substringAfter(BEARER)
        try {
            if (!jwtUtil.validateToken(token)) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expired or invalid")
                return
            }
            val claims = jwtUtil.getClaimsFromToken(token)
            val mobileNumber = claims[MOBILE_NUMBER] as String
//            request.setAttribute(MOBILE_NUMBER, mobileNumber)
            val authentication = JwtAuthenticationToken(mobileNumber)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token validation failed: ${e.message}")
            return
        }
        filterChain.doFilter(request, response)
    }


}

class JwtAuthenticationToken(private val mobileNumber: String) : AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any = ""
    override fun getPrincipal(): Any = mobileNumber
}

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Error: ${authException.message}")
    }
}

fun sendErrorResponse(response: HttpServletResponse, status: HttpStatus, message: String) {
    response.status = status.value()
    response.contentType = "application/json"
    response.writer.write("""{"code": ${status.value()}, "message": "$message"}""")
}

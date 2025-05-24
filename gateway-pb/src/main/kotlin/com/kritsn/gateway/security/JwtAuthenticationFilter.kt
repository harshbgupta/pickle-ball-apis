package com.kritsn.gateway.security

import com.kritsn.lib.jwt.JwtUtil
import com.kritsn.lib.jwt.URL_PREFIX_OPEN
import com.kritsn.lib.jwt.URL_PREFIX_PUBLIC
import com.kritsn.lib.jwt.URL_PREFIX_TOKEN
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
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
@Order(Ordered.HIGHEST_PRECEDENCE)
class JwtAuthenticationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {
    private val loggerr = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    private val excludedPaths = listOf(
        "$URL_PREFIX_PUBLIC/**",
        "$URL_PREFIX_OPEN/**",
        "$URL_PREFIX_TOKEN/**",
    )

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val isExcludedPath = excludedPaths.any { request.servletPath.startsWith(it) }
        loggerr.info("isExcludedPath: $isExcludedPath ,request url: ${request.servletPath}, request method: ${request.method}")
        return isExcludedPath
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        loggerr.info("Incoming request: method=${request.method}, URI=${request.requestURI}, Content-Type=${request.contentType}")
        // 1. Just checking if are these are public (open, token, public) end paths
        val requestURI = request.servletPath
        if (requestURI.startsWith(URL_PREFIX_OPEN)
            || requestURI.startsWith(URL_PREFIX_PUBLIC)
            || requestURI.startsWith(URL_PREFIX_TOKEN)
        ) {
            try {
                // Allowing public (open, token, public) end paths as they will not have headers
                filterChain.doFilter(request, response)
            } catch (e: Exception) {
                loggerr.error("Error during filter chain processing: ${e.message}", e)
                sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred")
            }
            return
        }
        // Authenticating NOT public end paths and they MUST have headers
        val tokenWithBearer = request.getHeader(HttpHeaders.AUTHORIZATION)
        loggerr.info("token => $tokenWithBearer")
        if (tokenWithBearer.isNullOrEmpty()) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Missing is null or empty")
            return
        }

        try {
            val claims = jwtUtil.validateTokenAndExtractClaims(tokenWithBearer)
            if (claims == null) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expired or invalid")
                return
            }

            val mobileNumber = claims.subject //claims[MOBILE_NUMBER] as String
//            request.setAttribute(MOBILE_NUMBER, mobileNumber)
            val authentication = JwtAuthenticationToken(tokenWithBearer, mobileNumber)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token validation failed: ${e.message}")
            return
        }
        filterChain.doFilter(request, response)
    }


}


class JwtAuthenticationToken(private val token: String, private val phoneNumber: String) : UsernamePasswordAuthenticationToken(null, null) {
    override fun getCredentials(): Any? = null
    override fun getPrincipal(): Any? = null

    fun getToken(): String {
        return token
    }
    fun getPhoneNumber(): String {
        return phoneNumber
    }
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
    response.writer.write("""{"success":false,"code": ${status.value()},"message": "$message","timestamp":${System.currentTimeMillis()}}""")
}

package com.kritsn.gateway.security.config

import com.kritsn.gateway.security.*
import com.kritsn.lib.jwt.JwtUtil
import com.kritsn.lib.jwt.URL_PREFIX_OPEN
import com.kritsn.lib.jwt.URL_PREFIX_PUBLIC
import com.kritsn.lib.jwt.URL_PREFIX_TOKEN
//import com.kritsn.gateway.security.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Copyright © 2025 Kritsn LLP. All rights reserved.
 *
 * @author Radhey (hr-sh)
 * @since May 12, 2025
 */
@EnableWebSecurity
@Configuration
class SecurityConfig {

    // Imp: as JwtUtil is part of different module
    @Bean
    fun jwtUtil(): JwtUtil {
        return JwtUtil()
    }

    @Autowired
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint? = null

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtUtil())
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("$URL_PREFIX_PUBLIC/**", "$URL_PREFIX_OPEN/**", "$URL_PREFIX_TOKEN/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint) }
            .headers { headers ->
                headers.cacheControl { cache ->
                    cache.disable() // Disable default cache control
                }
                /*headers.addHeaderWriter { request, response ->
                    response.setHeader("Cache-Control", "max-age=120, must-revalidate")
                }*/
            }
        return http.build()
    }

}
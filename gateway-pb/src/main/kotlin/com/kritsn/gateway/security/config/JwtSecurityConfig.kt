package com.kritsn.gateway.security.config

import com.kritsn.gateway.security.JwtAuthenticationEntryPoint
import com.kritsn.gateway.security.JwtAuthenticationTokenFilter
import lombok.RequiredArgsConstructor
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


/**
 * Copyright © 2021 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since January 16, 2021
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
class JwtSecurityConfig {
    @Autowired
    private val authenticationProvider: com.kritsn.gateway.security.JwtAuthenticationProvider? = null

    @Autowired
    private val entryPoint: JwtAuthenticationEntryPoint? = null

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf<AuthenticationProvider?>(authenticationProvider))
    }

    @Bean
    fun authenticationTokenFilter(): JwtAuthenticationTokenFilter {
        val filter = JwtAuthenticationTokenFilter()
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(com.kritsn.gateway.security.JwtSuccessHandler())
        return filter
    }


    @Bean
    protected fun configure(http: HttpSecurity): SecurityFilterChain {
        http.csrf { disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/open/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.headers { it.cacheControl { disable() } }
        return http.build()
    }
}

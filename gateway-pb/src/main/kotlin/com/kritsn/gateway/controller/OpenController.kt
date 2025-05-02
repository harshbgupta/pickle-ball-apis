package com.kritsn.gateway.controller

import com.kritsn.gateway.security.JwtGenerator
import com.kritsn.gateway.security.model.JwtUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Copyright © 2019 Sapiens Innospace. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since October 08, 2019
 */
@RequestMapping("/open")
@RestController
class OpenController {


    @Autowired
    lateinit var jwtGenerator: JwtGenerator

    @GetMapping("/dummy")
    private fun dummyApi(): String {
        return "this is a dummy api"
    }
    @GetMapping("/token/{phoneNumber}")
    private fun generate(@PathVariable("phoneNumber") mob:String): String {
        println("method called...")
        val jwtUser = JwtUser().apply {
            id = 1L
            phoneNumber = "9457825354"
            email ="harsh@gmail.com"
            userType = "2"
        }

        return jwtGenerator.generate(mob,"admin")
    }
}
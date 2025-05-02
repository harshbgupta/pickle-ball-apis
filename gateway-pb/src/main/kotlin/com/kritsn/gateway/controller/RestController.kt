package com.kritsn.gateway.controller

import com.kritsn.gateway.security.JwtGenerator
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
@RequestMapping("/rest")
@RestController
class RestController {


    @Autowired
    lateinit var jwtGenerator: JwtGenerator

    @GetMapping("token/{phoneNumber}")
    private fun generate(@PathVariable("phoneNumber") mob:String): String {
        return jwtGenerator.generate(mob,"user")
    }
}
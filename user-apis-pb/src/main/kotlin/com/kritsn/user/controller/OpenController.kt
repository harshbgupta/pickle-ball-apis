package com.kritsn.user.controller

import com.kritsn.lib.base.Response
import com.kritsn.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("open")
class OpenController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/user/exist/{mobileNumber}")
    fun checkUserExistence(@PathVariable("mobileNumber") mobileNumber:String?): Response<Boolean> {
        return userService.checkUserExistence(mobileNumber)
    }

    @GetMapping("/user/sendOtp/{mobileNumber}")
    fun sendOtp(@PathVariable("mobileNumber") mobileNumber:String?):Response<String>{
        return userService.sendOtp(mobileNumber)
    }

    @GetMapping("/user/sendOtp/{mobileNumber}/{otpKey}")
    fun sendOtp(@PathVariable("mobileNumber") mobileNumber:String?,@PathVariable("otpKey") otpKey:String?):Response<String>{
        return userService.verifyOtp(mobileNumber, otpKey)
    }
}
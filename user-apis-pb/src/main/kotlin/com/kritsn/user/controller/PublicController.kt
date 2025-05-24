package com.kritsn.user.controller

import com.kritsn.lib.base.Response
import com.kritsn.user.model.ReqVerifyOtp
import com.kritsn.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public")
class PublicController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/exist/{mobileNumber}")
    fun checkUserExistence(@PathVariable("mobileNumber") mobileNumber:String?): Response<Boolean> {
        return userService.checkUserExistence(mobileNumber)
    }

    @GetMapping("/sendOtp/{mobileNumber}")
    fun sendOtp(@PathVariable("mobileNumber") mobileNumber:String?):Response<String>{
        return userService.sendOtp(mobileNumber)
    }

    @PostMapping("/verifyOTP")
    fun verifyOtp(@RequestBody req: ReqVerifyOtp):Response<String>{
        return userService.verifyOtp(req.mobileNumber, req.otpKey)
    }
}
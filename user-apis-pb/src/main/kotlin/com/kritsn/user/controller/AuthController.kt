package com.kritsn.user.controller

import com.kritsn.user.base.Response
import com.kritsn.user.model.ReqUser
import com.kritsn.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun dummyApi(): String {
        return "Its Dummy Api"
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody reqUser: ReqUser): Response<String> {
        return userService.createUser(reqUser)
    }

}
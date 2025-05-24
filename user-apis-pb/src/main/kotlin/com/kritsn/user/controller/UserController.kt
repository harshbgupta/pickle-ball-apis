package com.kritsn.user.controller

import com.kritsn.lib.base.Response
import com.kritsn.user.dto.User
import com.kritsn.user.model.ReqUser
import com.kritsn.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody req: ReqUser): Response<User> {
        return userService.createUser(req)
    }
}
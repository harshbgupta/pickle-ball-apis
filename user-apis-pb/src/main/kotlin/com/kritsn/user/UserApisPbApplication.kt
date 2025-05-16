package com.kritsn.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class UserApisPbApplication

fun main(args: Array<String>) {
    runApplication<UserApisPbApplication>(*args)
}

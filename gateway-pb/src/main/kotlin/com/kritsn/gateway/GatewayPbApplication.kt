package com.kritsn.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication/*(scanBasePackages = ["com.kritsn.lib", "com.kritsn.gateway"])*/
class GatewayPbApplication

fun main(args: Array<String>) {
    runApplication<GatewayPbApplication>(*args)
}

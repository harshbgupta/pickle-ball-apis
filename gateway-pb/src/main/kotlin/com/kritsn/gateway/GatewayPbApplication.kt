package com.kritsn.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewayPbApplication

fun main(args: Array<String>) {
    runApplication<GatewayPbApplication>(*args)
}

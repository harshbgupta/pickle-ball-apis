package com.kritsn.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class DiscoveryServerPbApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryServerPbApplication>(*args)
}

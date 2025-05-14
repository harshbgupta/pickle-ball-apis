package com.kritsn.user.feign

import com.kritsn.user.model.ReqUser
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("GATEWAY-PB")
interface GatewayFeign {

    @PostMapping("/gateway/token")
    fun generateToken(@RequestBody reqUser: ReqUser): String
}
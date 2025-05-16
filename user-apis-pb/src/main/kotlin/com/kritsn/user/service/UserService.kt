package com.kritsn.user.service

import com.kritsn.lib.base.Response
import com.kritsn.lib.base.buildErrorResponse
import com.kritsn.lib.base.buildSuccessResponse
import com.kritsn.user.dto.User
import com.kritsn.user.feign.GatewayFeign
import com.kritsn.user.model.ReqUser
import com.kritsn.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.random.Random


@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var gatewayFeign: GatewayFeign

    fun checkUserExistence(mobileNumber: String?): Response<Boolean> {
        mobileNumber ?: return buildErrorResponse("mobile number is required.", HttpStatus.BAD_REQUEST)
        val users = userRepository.findUserByMobileNumber(mobileNumber)
        return buildSuccessResponse(users.isNotEmpty())
    }

    fun sendOtp(mobileNumber: String?): Response<String> {
        mobileNumber ?: return buildErrorResponse("mobile number is required.", HttpStatus.BAD_REQUEST)
        //sed otp using 3rd party service
        //as of now I am sending from my own
        val numberBetweenZeroToOneMillion = Random.nextInt(0, 1000000) // Generates a number between 100000 and 999999
        val sizeDigitOtp =
            numberBetweenZeroToOneMillion.toString().padStart(6, '0') // Ensures 6 digits with leading zeros if needed
        return buildSuccessResponse(sizeDigitOtp)
    }

    fun verifyOtp(mobileNumber: String?, otpKey: String?): Response<String> {
        mobileNumber ?: return buildErrorResponse("mobile number is required.", HttpStatus.BAD_REQUEST)
        otpKey ?: return buildErrorResponse("otpKey is required.", HttpStatus.BAD_REQUEST)

        //verify OTP with same 3rd party service
        if(!verifyOtpWithThirdParty(mobileNumber, otpKey)){
            return buildErrorResponse("Invalid OTP.", HttpStatus.UNAUTHORIZED)
        }
        //get auth token
        val users = userRepository.findUserByMobileNumber(mobileNumber)
        if (users.isEmpty()) {
            return buildErrorResponse("No user found associated with this mobile number.", HttpStatus.NOT_FOUND)
        }
        val token = gatewayFeign.generateToken(ReqUser(users.first()))
        return buildSuccessResponse(token)
    }

    private fun verifyOtpWithThirdParty(mobileNumber: String, otpKey: String):Boolean {
        //as of now I am verifying the OTP not
        return true
    }

    fun createUser(reqUser: ReqUser): Response<String> {
        val user = User().apply {
            firstName = reqUser.firstName
            lastName = reqUser.lastName
            mobileNumber = reqUser.mobileNumber
            email = reqUser.email
        }
        userRepository.save(user)
        return buildSuccessResponse()
    }
}
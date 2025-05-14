package com.kritsn.user.service

import com.kritsn.user.base.Response
import com.kritsn.user.base.throwErrorResponse
import com.kritsn.user.base.throwSuccessResponse
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
        mobileNumber ?: return throwErrorResponse("mobile number is required.", HttpStatus.BAD_REQUEST.value())
        val users = userRepository.findUserByMobileNumber(mobileNumber)
        return throwSuccessResponse(HttpStatus.CREATED.value(), users.isNotEmpty())
    }

    fun sendOtp(mobileNumber: String?): Response<String> {
        mobileNumber ?: return throwErrorResponse("mobile number is required.", HttpStatus.BAD_REQUEST.value())
        //sed otp using 3rd party service
        //as of now I am sending from my own
        val numberBetweenZeroToOneMillion = Random.nextInt(0, 1000000) // Generates a number between 100000 and 999999
        val sizeDigitOtp =
            numberBetweenZeroToOneMillion.toString().padStart(6, '0') // Ensures 6 digits with leading zeros if needed
        return throwSuccessResponse(HttpStatus.CREATED.value(), sizeDigitOtp)
    }

    fun verifyOtp(mobileNumber: String?, otpKey: String?): Response<String> {
        mobileNumber ?: return throwErrorResponse("mobile number is required.", HttpStatus.BAD_REQUEST.value())
        otpKey ?: return throwErrorResponse("otpKey is required.", HttpStatus.BAD_REQUEST.value())

        //verify OTP with same 3rd party service
        if(!verifyOtpWithThirdParty(mobileNumber, otpKey)){
            return throwErrorResponse("Invalid OTP.", HttpStatus.UNAUTHORIZED.value())
        }
        //get auth token
        val users = userRepository.findUserByMobileNumber(mobileNumber)
        if (users.isEmpty()) {
            return throwErrorResponse("No user found associated with this mobile number.", HttpStatus.NOT_FOUND.value())
        }
        val token = gatewayFeign.generateToken(ReqUser(users.first()))
        return throwSuccessResponse(HttpStatus.CREATED.value(), token)
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
        return throwSuccessResponse(HttpStatus.CREATED.value(), null)
    }
}
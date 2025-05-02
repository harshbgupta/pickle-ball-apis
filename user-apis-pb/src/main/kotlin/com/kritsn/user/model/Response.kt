package com.kritsn.user.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import org.springframework.http.HttpStatus

@Builder
@NoArgsConstructor
@AllArgsConstructor

open class Response<T> {
    private var successful: Boolean
    private var httpStatus: Int? = null
    private var message: String
    private var error: String?
    private var timestamp: String
    private var result: T?

    //SUCCESS CONSTRUCTOR
    constructor(httpStatus: Int?, successful: Boolean?) {
        this.httpStatus = httpStatus ?: HttpStatus.BAD_REQUEST.value()
        this.successful = successful != null && successful
        this.message = "Success"
        this.timestamp = System.currentTimeMillis().toString() + ""
        this.error = null
        this.result = null
    }

    //SUCCESS CONSTRUCTOR
    constructor(httpStatus: Int?, successful: Boolean?, message: String?) {
        this.httpStatus = httpStatus ?: HttpStatus.BAD_REQUEST.value()
        this.successful = successful != null && successful
        this.message = message ?: "Success"
        this.timestamp = System.currentTimeMillis().toString() + ""
        this.error = null
        this.result = null
    }

    //Failed Constructor
    constructor(errorMessage: String?) {
        this.successful = false
        this.message = errorMessage ?: "Failed"
        this.timestamp = System.currentTimeMillis().toString() + ""
        this.error = errorMessage ?: ""
        this.result = null
    }

    fun getSuccessful(): Boolean {
        return successful
    }

    fun setSuccessful(successful: Boolean) {
        this.successful = successful
    }

    fun getHttpStatus(): Int? {
        return httpStatus
    }

    fun setHttpStatus(httpStatus: Int?) {
        this.httpStatus = httpStatus
    }

    fun getMessage(): String {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getError(): String? {
        return error
    }

    fun setError(error: String?) {
        this.error = error
    }

    fun getTimestamp(): String {
        return timestamp
    }

    fun setTimestamp(timestamp: String) {
        this.timestamp = timestamp
    }

    fun getResult(): T? {
        return result
    }

    fun setResult(result: T?) {
        this.result = result
    }
}
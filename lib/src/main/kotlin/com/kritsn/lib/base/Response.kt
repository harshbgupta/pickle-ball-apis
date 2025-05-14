package com.kritsn.user.base

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.http.HttpStatus

const val MESSAGE_SUCCESS = "Success"
const val MESSAGE_FAILED = "Failed"

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class Response<T> {
    var successful: Boolean? = null
    var httpStatus: Int? = null
    var message: String? = ""
    var timestamp: Long = System.currentTimeMillis()
    var result: T? = null
}

fun <T> throwSuccessResponse(httpStatus: Int?, result: T? = null): Response<T> {
    val response = Response<T>()
    response.httpStatus = httpStatus ?: HttpStatus.OK.value()
    response.successful = true
    response.message = MESSAGE_SUCCESS
    response.timestamp = System.currentTimeMillis()
    response.result = result
    return response
}

fun <T> throwErrorResponse(errorMessage: String?, httpStatus: Int? = null): Response<T> {
    val response = Response<T>()
    response.httpStatus = httpStatus ?: HttpStatus.BAD_REQUEST.value()
    response.successful = false
    response.message = errorMessage ?: MESSAGE_FAILED
    response.timestamp = System.currentTimeMillis()
    response.result = null
    return response
}

fun <T> throwCustomResponse(
    httpStatus: Int?,
    successful: Boolean?,
    message: String? = null,
    result: T? = null
): Response<T> {
    val response = Response<T>()
    response.httpStatus = httpStatus ?: HttpStatus.BAD_REQUEST.value()
    response.successful = successful == true //ensuring null safety
    response.message = if (successful == true) message ?: MESSAGE_SUCCESS else message ?: MESSAGE_FAILED
    response.timestamp = System.currentTimeMillis()
    response.result = result
    return response
}

fun <T> throwInternalServerErrorResponse(e: Throwable? = null): Response<T> {
    val response = Response<T>()
    response.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value()
    response.successful = false
    response.message = e?.message ?: MESSAGE_FAILED
    response.timestamp = System.currentTimeMillis()
    response.result = null
    e?.printStackTrace() //printing exception trace
    return response
}
package com.kritsn.lib.base

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.http.HttpStatus

const val MESSAGE_SUCCESS = "Success"
const val MESSAGE_FAILED = "Failed"
const val MESSAGE_SERVER_ERROR = "Server Error"

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class Response<T> {
    var success: Boolean? = null
    var code: Int? = null
    var message: String? = ""
    var timestamp: Long = System.currentTimeMillis()
    var data: T? = null
}

fun <T> createSuccessResponse(data: T? = null): Response<T> {
    val response = Response<T>()
    response.code = HttpStatus.OK.value()
    response.success = true
    response.message = MESSAGE_SUCCESS
    response.timestamp = System.currentTimeMillis()
    response.data = data
    return response
}

fun <T> createCustomResponse(
    httpStatus: HttpStatus?,
    successful: Boolean?,
    message: String? = null,
    data: T? = null
): Response<T> {
    val response = Response<T>()
    response.code = httpStatus?.value() ?: HttpStatus.BAD_REQUEST.value()
    response.success = successful == true //ensuring null safety
    response.message = if (successful == true) message ?: MESSAGE_SUCCESS else message ?: MESSAGE_FAILED
    response.timestamp = System.currentTimeMillis()
    response.data = data
    return response
}

fun <T> createErrorResponse(errorMessage: String?, httpStatus: HttpStatus? = null): Response<T> {
    val response = Response<T>()
    response.code = httpStatus?.value() ?: HttpStatus.BAD_REQUEST.value()
    response.success = false
    response.message = errorMessage ?: MESSAGE_FAILED
    response.timestamp = System.currentTimeMillis()
    response.data = null
    return response
}

fun <T> createServerErrorResponse(e: Throwable? = null): Response<T> {
    val response = Response<T>()
    response.code = HttpStatus.INTERNAL_SERVER_ERROR.value()
    response.success = false
    response.message = e?.message ?: MESSAGE_SERVER_ERROR
    response.timestamp = System.currentTimeMillis()
    response.data = null
    e?.printStackTrace() //printing exception trace
    return response
}
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
    var uiMessage: String? = null
    var timestamp: Long = System.currentTimeMillis()
    var data: T? = null
}

fun <T> buildSuccessResponse(data: T? = null, code: HttpStatus? = null, uiMessage: String? = null): Response<T> {
    val response = Response<T>()
    response.code = code?.value() ?: HttpStatus.OK.value()
    response.success = true
    response.message = MESSAGE_SUCCESS
    response.uiMessage = uiMessage
    response.timestamp = System.currentTimeMillis()
    response.data = data
    return response
}

fun <T> buildCustomResponse(
    httpStatus: HttpStatus?,
    successful: Boolean?,
    message: String? = null,
    data: T? = null,
    uiMessage: String? = null
): Response<T> {
    val response = Response<T>()
    response.code = httpStatus?.value() ?: HttpStatus.BAD_REQUEST.value()
    response.success = successful == true //ensuring null safety
    response.message = if (successful == true) message ?: MESSAGE_SUCCESS else message ?: MESSAGE_FAILED
    response.uiMessage = uiMessage
    response.timestamp = System.currentTimeMillis()
    response.data = data
    return response
}

fun <T> buildErrorResponse(
    message: String?,
    httpStatus: HttpStatus? = null,
    uiMessage: String? = null
): Response<T> {
    val response = Response<T>()
    response.code = httpStatus?.value() ?: HttpStatus.BAD_REQUEST.value()
    response.success = false
    response.message = message ?: MESSAGE_FAILED
    response.uiMessage = uiMessage
    response.timestamp = System.currentTimeMillis()
    response.data = null
    return response
}

fun <T> buildServerErrorResponse(e: Throwable? = null): Response<T> {
    val response = Response<T>()
    response.code = HttpStatus.INTERNAL_SERVER_ERROR.value()
    response.success = false
    response.message = e?.message ?: MESSAGE_SERVER_ERROR
    response.uiMessage = "Something went wrong, Please try again after some time."
    response.timestamp = System.currentTimeMillis()
    response.data = null

    //printing exception trace before sending the response
    e?.printStackTrace()
    return response
}
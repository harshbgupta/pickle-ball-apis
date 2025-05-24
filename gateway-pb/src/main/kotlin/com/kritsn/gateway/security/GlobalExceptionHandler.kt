package com.kritsn.gateway.security

import com.kritsn.lib.base.buildErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.format.DateTimeParseException

/**
 * Copyright © 2025 Kritsn LLP. All rights reserved.
 *
 * @author Radhey (hr-sh)
 * @since May 21, 2025
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(ex: NoHandlerFoundException): ResponseEntity<Any> {
        ex.printStackTrace()
        val response = buildErrorResponse<Any>(
            message = "The requested URL ${ex.requestURL} does not exist",
            httpStatus = HttpStatus.NOT_FOUND,
        )
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        ex.printStackTrace()
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        val errorMessage = ""
        errors.values.map { error -> if (errorMessage.isNotEmpty() || error.isNotEmpty()) "$error, $errorMessage" }
        val response = buildErrorResponse<Map<String, String>>(
            message = "${ex.message}, errorMessage",
            httpStatus = HttpStatus.BAD_REQUEST,
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(ex: DateTimeParseException): ResponseEntity<Any> {
        ex.printStackTrace()
        val response = buildErrorResponse<Any>(
            message = "Invalid date/time format: ${ex.message}",
            httpStatus = HttpStatus.BAD_REQUEST,
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Any> {
        ex.printStackTrace()
        val response = buildErrorResponse<Any>(
            message = "Invalid argument: ${ex.message}",
            httpStatus = HttpStatus.BAD_REQUEST,
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(org.springframework.security.access.AuthorizationServiceException::class)
    fun handleAuthorizationServiceException(ex: org.springframework.security.access.AuthorizationServiceException): ResponseEntity<Any> {
        ex.printStackTrace()
        val response = buildErrorResponse<Any>(
            message = "Authorization error: ${ex.message}",
            httpStatus = HttpStatus.FORBIDDEN,
        )
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(ex: org.springframework.security.access.AccessDeniedException): ResponseEntity<Any> {
        ex.printStackTrace()
        val response = buildErrorResponse<Any>(
            message = "Access denied: ${ex.message}",
            httpStatus = HttpStatus.FORBIDDEN,
        )
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }
}
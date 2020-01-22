package com.valensas.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ApiException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionErrorHandler(
    mapper: ObjectMapper,
    log4xx: Boolean,
    log5xx: Boolean,
    private val debug: Boolean,
    private val debugPackages: List<String>
) : HttpErrorHandler(
    mapper,
    log4xx = log4xx,
    log5xx = log5xx
) {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(exception: ApiException): ResponseEntity<Any> {
        val statusCode = exception.statusCode
        val body = convert(exception, null, debug, debugPackages)
        return ResponseEntity(body, statusCode)
    }
}

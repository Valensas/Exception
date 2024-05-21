package com.valensas.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.exception.ApiException
import com.valensas.exception.autoconfigure.ExceptionHandlerConfigurationProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionErrorHandler(
    mapper: ObjectMapper,
    private val debugProperties: ExceptionHandlerConfigurationProperties
) : HttpErrorHandler(
        mapper,
        log4xx = debugProperties.logger.log4xx,
        log5xx = debugProperties.logger.log5xx
    ) {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(exception: ApiException): ResponseEntity<Any> {
        val statusCode = exception.statusCode
        val body = convert(exception, null, debugProperties.debug.enabled, debugProperties.debug.packages)
        return ResponseEntity(body, statusCode)
    }
}

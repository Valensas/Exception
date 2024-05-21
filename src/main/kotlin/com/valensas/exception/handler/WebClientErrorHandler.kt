package com.valensas.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.exception.ApiException
import com.valensas.exception.autoconfigure.ExceptionHandlerConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClientResponseException

@RestControllerAdvice
class WebClientErrorHandler(
    mapper: ObjectMapper,
    private val debugProperties: ExceptionHandlerConfigurationProperties
) : HttpErrorHandler(
        mapper,
        log4xx = debugProperties.logger.log4xx,
        log5xx = debugProperties.logger.log5xx
    ) {
    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientException(exception: WebClientResponseException): ResponseEntity<Any> {
        val statusCode = exception.statusCode.value().let(HttpStatus::valueOf)
        return try {
            val apiException = mapper.readValue(exception.responseBodyAsString, ApiException::class.java)
            val body = convert(apiException, exception, debugProperties.debug.enabled, debugProperties.debug.packages)

            responseWith(body, statusCode)
        } catch (e: Throwable) {
            // Try to return exception as ResponseEntity with keeping data
            val body = exception.responseBodyAsString.ifEmpty { mapOf("message" to (exception.message ?: exception.localizedMessage)) }
            responseWith(body, statusCode)
        }
    }
}

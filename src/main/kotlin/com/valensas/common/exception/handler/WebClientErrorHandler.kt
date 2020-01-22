package com.valensas.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ApiException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClientResponseException

@RestControllerAdvice
class WebClientErrorHandler(
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
    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientException(exception: WebClientResponseException): ResponseEntity<Any> {
        val statusCode = exception.statusCode
        return try {
            val apiException = mapper.readValue(exception.responseBodyAsString, ApiException::class.java)
            val body = convert(apiException, exception, debug, debugPackages)

            responseWith(body, statusCode)
        } catch (e: Throwable) {
            // Try to return exception as ResponseEntity with keeping data
            val body = exception.responseBodyAsString.ifEmpty { mapOf("message" to (exception.message ?: exception.localizedMessage)) }
            responseWith(body, statusCode)
        }
    }
}

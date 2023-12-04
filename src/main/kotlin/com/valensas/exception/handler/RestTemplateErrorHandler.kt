package com.valensas.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.exception.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpStatusCodeException

@RestControllerAdvice
class RestTemplateErrorHandler(
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
    @ExceptionHandler(HttpStatusCodeException::class)
    fun handleHttpStatusCodeException(exception: HttpStatusCodeException): ResponseEntity<Any> {
        val statusCode = exception.statusCode.value().let(HttpStatus::valueOf)
        return try {
            val apiException = mapper.readValue(exception.responseBodyAsString, ApiException::class.java)
            val body = convert(apiException, exception, debug, debugPackages)

            responseWith(body, statusCode)
        } catch (e: Throwable) {
            responseWith(exception.responseBodyAsString, statusCode)
        }
    }
}

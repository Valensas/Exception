package com.valensas.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ApiException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpStatusCodeException

@ControllerAdvice
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
        return try {
            val apiException = mapper.readValue(exception.responseBodyAsString, ApiException::class.java)
            val body = convert(apiException, exception, debug, debugPackages)
            responseWith(body, exception.statusCode)
        } catch (e: Throwable) {
            responseWith(exception.responseBodyAsString, exception.statusCode)
        }
    }
}

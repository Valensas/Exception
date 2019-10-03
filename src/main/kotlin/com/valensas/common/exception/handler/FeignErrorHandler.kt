package com.valensas.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ApiException
import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class FeignErrorHandler(
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
    @ExceptionHandler(FeignException::class)
    fun handleFeignException(exception: FeignException): ResponseEntity<Any> {
        val statusCode = HttpStatus.resolve(exception.status()) ?: HttpStatus.INTERNAL_SERVER_ERROR
        return try {
            val apiException = mapper.readValue(exception.content(), ApiException::class.java)
            val body = convert(apiException, exception, debug, debugPackages)

            responseWith(body, statusCode)
        } catch (e: Throwable) {
            // Try to return exception as ResponseEntity with keeping data
            val body = exception.content() ?: mapOf("message" to (exception.message ?: exception.localizedMessage))
            responseWith(body, statusCode)
        }
    }
}

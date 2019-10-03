package com.valensas.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
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
        val statusCode = statusCodeForException(exception)
        val body = convert(exception, null, debug, debugPackages)
        return ResponseEntity(body, statusCode)
    }

    private fun statusCodeForException(e: ApiException): HttpStatus {
        var klass: Class<*>? = e.javaClass

        while (klass != null) {
            val annotations = klass.getAnnotationsByType(ResponseStatus::class.java)
            val statusCode = annotations.firstOrNull()?.value
            if (statusCode != null) {
                return statusCode
            }
            klass = klass.superclass
        }

        return HttpStatus.INTERNAL_SERVER_ERROR
    }
}

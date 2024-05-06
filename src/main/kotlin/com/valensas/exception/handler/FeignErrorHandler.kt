package com.valensas.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.exception.ApiException
import feign.FeignException
import feign.FeignException.FeignClientException
import feign.FeignException.FeignServerException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
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
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(FeignException::class)
    fun handleFeignException(exception: FeignException): ResponseEntity<Any> {
        return try {
            val apiException = mapper.readValue(exception.contentUTF8(), ApiException::class.java)
            val body = convert(apiException, exception, debug, debugPackages)
            responseWith(body, apiException.statusCode)
        } catch (e: FeignClientException) {
            val body = exception.contentUTF8() ?: mapOf("message" to (exception.message ?: exception.localizedMessage))
            responseWith(body, HttpStatus.resolve(exception.status()) ?: HttpStatus.BAD_REQUEST)
        } catch (e: FeignServerException) {
            val body = exception.contentUTF8() ?: mapOf("message" to (exception.message ?: exception.localizedMessage))
            responseWith(body, HttpStatus.resolve(exception.status()) ?: HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: Throwable) {
            logger.warn("Unhandled FeignException occurred. Response body: {}.", exception.contentUTF8(), e)
            responseWith(mapOf("message" to "An error occured."), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}

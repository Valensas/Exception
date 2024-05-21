package com.valensas.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.exception.FieldError
import com.valensas.exception.ParameterErrorCode
import com.valensas.exception.ParameterException
import com.valensas.exception.autoconfigure.ExceptionHandlerConfigurationProperties
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class WebParameterExceptionErrorHandler(
    mapper: ObjectMapper,
    debugProperties: ExceptionHandlerConfigurationProperties
) : HttpErrorHandler(
        mapper,
        log4xx = debugProperties.logger.log4xx,
        log5xx = debugProperties.logger.log5xx
    ) {
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        exception: MissingServletRequestParameterException,
        webRequest: HttpServletRequest
    ): ResponseEntity<Any> {
        val status = exception.statusCode as HttpStatus
        val parameterException =
            ParameterException(
                id = webRequest.requestId,
                path = webRequest.requestURI,
                message = exception.message,
                code = ParameterErrorCode.Invalid
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        exception: MethodArgumentTypeMismatchException,
        webRequest: HttpServletRequest
    ): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val parameterException =
            ParameterException(
                id = webRequest.requestId,
                path = webRequest.requestURI,
                message = exception.message ?: "Param is wrong",
                code = ParameterErrorCode.Invalid
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(
        exception: MissingRequestHeaderException,
        webRequest: HttpServletRequest
    ): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val parameterException =
            ParameterException(
                id = webRequest.requestId,
                path = webRequest.requestURI,
                message = exception.message,
                code = ParameterErrorCode.Invalid
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException,
        webRequest: HttpServletRequest
    ): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val parameterException =
            ParameterException(
                id = webRequest.requestId,
                path = webRequest.requestURI,
                message = exception.message ?: "Http message not readable",
                code = ParameterErrorCode.Invalid
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        webRequest: HttpServletRequest
    ): ResponseEntity<Any> {
        val status = exception.statusCode as HttpStatus
        val parameterException =
            ParameterException(
                id = webRequest.requestId,
                path = webRequest.requestURI,
                message = exception.message,
                code = ParameterErrorCode.Invalid,
                fieldErrors =
                    exception.fieldErrors.map {
                        FieldError(
                            field = it.field,
                            message = it.defaultMessage
                        )
                    }
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }
}

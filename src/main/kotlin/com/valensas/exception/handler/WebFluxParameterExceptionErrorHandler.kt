package com.valensas.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.valensas.exception.FieldError
import com.valensas.exception.ParameterErrorCode
import com.valensas.exception.ParameterException
import org.springframework.beans.TypeMismatchException
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.server.MissingRequestValueException
import org.springframework.web.server.ServerWebInputException

@RestControllerAdvice
class WebFluxParameterExceptionErrorHandler(
    mapper: ObjectMapper,
    log4xx: Boolean,
    log5xx: Boolean
) : HttpErrorHandler(
        mapper,
        log4xx = log4xx,
        log5xx = log5xx
    ) {
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebExchangeBindException(
        exception: WebExchangeBindException,
        webRequest: ServerHttpRequest
    ): ResponseEntity<Any> {
        val status = exception.statusCode as HttpStatus
        val parameterException =
            ParameterException(
                id = webRequest.id,
                path = webRequest.path.toString(),
                message = exception.reason ?: "Request Body Validation failure",
                parameter = exception.bindingResult.objectName,
                code = ParameterErrorCode.Invalid,
                fieldErrors =
                    exception.bindingResult.fieldErrors.map {
                        FieldError(
                            field = it.field,
                            message = it.defaultMessage
                        )
                    }
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }

    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInputException(
        exception: ServerWebInputException,
        webRequest: ServerHttpRequest
    ): ResponseEntity<Any> {
        val status = exception.statusCode as HttpStatus
        var parameter: String? = null
        var message = exception.message

        when (exception.cause) {
            is TypeMismatchException -> {
                val typeMismatchException = exception.cause as TypeMismatchException
                parameter = typeMismatchException.propertyName
                message = typeMismatchException.localizedMessage
            }
            is DecodingException -> {
                val decodingException = exception.cause as DecodingException
                when (decodingException.cause) {
                    is MismatchedInputException -> {
                        val mismatchedInputException = decodingException.cause as MismatchedInputException
                        parameter =
                            mismatchedInputException.path.joinToString(".") {
                                it.fieldName
                            }
                        message = mismatchedInputException.originalMessage
                    }
                }
            }
        }

        val parameterException =
            ParameterException(
                id = webRequest.id,
                path = webRequest.path.toString(),
                message = message,
                parameter = parameter,
                code = ParameterErrorCode.Invalid
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }

    @ExceptionHandler(MissingRequestValueException::class)
    fun handleMissingRequestValueException(
        exception: MissingRequestValueException,
        webRequest: ServerHttpRequest
    ): ResponseEntity<Any> {
        val parameterException =
            ParameterException(
                id = webRequest.id,
                path = webRequest.path.toString(),
                message = exception.reason ?: "Missing Parameter",
                parameter = exception.name,
                code = ParameterErrorCode.Missing
            )
        val body = convert(parameterException)
        return responseWith(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(
        exception: HandlerMethodValidationException,
        webRequest: ServerHttpRequest
    ): ResponseEntity<Any> {
        val status = exception.statusCode as HttpStatus
        val parameterException =
            ParameterException(
                id = webRequest.id,
                path = webRequest.path.toString(),
                message = exception.reason ?: "Parameter Validation failure",
                code = ParameterErrorCode.Invalid,
                fieldErrors =
                    exception.valueResults.map {
                        FieldError(
                            field = it.methodParameter.parameter.name,
                            message = it.resolvableErrors.first().defaultMessage.toString()
                        )
                    }
            )
        val body = convert(parameterException)
        return responseWith(body, status)
    }
}

package com.valensas.common.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
open class ApiException(
    message: String,
    val code: String,
    val data: Any? = null,
    val trace: List<String>? = null,
    val `class`: String? = null
) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest(message: String, code: String = "BAD_REQUEST") : ApiException(message, code)

@ResponseStatus(HttpStatus.FORBIDDEN)
class Forbidden(message: String, code: String = "FORBIDDEN") : ApiException(message, code)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized(message: String, code: String = "UNAUTHORIZED") : ApiException(message, code)

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFound(message: String, code: String = "NOT_FOUND") : ApiException(message, code)

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class UnprocessableEntity(message: String, code: String = "UNPROCESSABLE_ENTITY") : ApiException(message, code)

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerError(message: String, code: String = "INTERNAL_SERVER_ERROR") : ApiException(message, code)

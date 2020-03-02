package com.valensas.common.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
open class ApiException(
    message: String,
    val code: String,
    val data: HashMap<*, *>? = null,
    val trace: List<String>? = null,
    val `class`: String? = null
) : RuntimeException(message) {
    val statusCode: HttpStatus by lazy {
        var klass: Class<*>? = javaClass

        while (klass != null) {
            val annotations = klass.getAnnotationsByType(ResponseStatus::class.java)
            annotations.firstOrNull()?.value?.let {
                return@lazy it
            }

            klass = klass.superclass
        }

        return@lazy HttpStatus.INTERNAL_SERVER_ERROR
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class BadRequest(message: String, code: String = "BAD_REQUEST", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.FORBIDDEN)
open class Forbidden(message: String, code: String = "FORBIDDEN", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
open class Unauthorized(message: String, code: String = "UNAUTHORIZED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NOT_FOUND)
open class NotFound(message: String, code: String = "NOT_FOUND", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
open class UnprocessableEntity(message: String, code: String = "UNPROCESSABLE_ENTITY", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
open class InternalServerError(message: String, code: String = "INTERNAL_SERVER_ERROR", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

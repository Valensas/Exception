package com.valensas.exception

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

@ResponseStatus(HttpStatus.CONTINUE)
open class Continue(message: String, code: String = "CONTINUE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.SWITCHING_PROTOCOLS)
open class SwitchingProtocols(message: String, code: String = "SWITCHING_PROTOCOLS", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PROCESSING)
open class Processing(message: String, code: String = "PROCESSING", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.CHECKPOINT)
open class CheckPoint(message: String, code: String = "CHECKPOINT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.OK)
open class Ok(message: String, code: String = "OK", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.CREATED)
open class Created(message: String, code: String = "CREATED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.ACCEPTED)
open class Accepted(message: String, code: String = "ACCEPTED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
open class NonAuthoritativeInformation(message: String, code: String = "NON_AUTHORITATIVE_INFORMATION", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NO_CONTENT)
open class NoContent(message: String, code: String = "NO_CONTENT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.RESET_CONTENT)
open class ResetContent(message: String, code: String = "RESET_CONTENT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PARTIAL_CONTENT)
open class PartialContent(message: String, code: String = "PARTIAL_CONTENT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.MULTI_STATUS)
open class MultiStatus(message: String, code: String = "MULTI_STATUS", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
open class AlreadyReported(message: String, code: String = "ALREADY_REPORTED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.IM_USED)
open class IMUsed(message: String, code: String = "IM_USED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.MULTIPLE_CHOICES)
open class MultipleChoices(message: String, code: String = "MULTIPLE_CHOICES", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
open class MovedPermanently(message: String, code: String = "MOVED_PERMANENTLY", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.FOUND)
open class Found(message: String, code: String = "FOUND", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.SEE_OTHER)
open class SeeOther(message: String, code: String = "SEE_OTHER", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NOT_MODIFIED)
open class NotModified(message: String, code: String = "NOT_MODIFIED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
open class TemporaryRedirect(message: String, code: String = "TEMPORARY_REDIRECT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
open class PermanentRedirect(message: String, code: String = "PERMANENT_REDIRECT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.BAD_REQUEST)
open class BadRequest(message: String, code: String = "BAD_REQUEST", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
open class Unauthorized(message: String, code: String = "UNAUTHORIZED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
open class PaymentRequired(message: String, code: String = "PAYMENT_REQUIRED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.FORBIDDEN)
open class Forbidden(message: String, code: String = "FORBIDDEN", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NOT_FOUND)
open class NotFound(message: String, code: String = "NOT_FOUND", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
open class MethodNotAllowed(message: String, code: String = "METHOD_NOT_ALLOWED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
open class NotAcceptable(message: String, code: String = "NOT_ACCEPTABLE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
open class ProxyAuthenticationRequired(message: String, code: String = "PROXY_AUTHENTICATION_REQUIRED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
open class RequestTimeout(message: String, code: String = "REQUEST_TIMEOUT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.CONFLICT)
open class Conflict(message: String, code: String = "CONFLICT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.GONE)
open class Gone(message: String, code: String = "GONE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.LENGTH_REQUIRED)
open class LengthRequired(message: String, code: String = "LENGTH_REQUIRED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
open class PreconditionFailed(message: String, code: String = "PRECONDITION_FAILED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
open class PayloadTooLarge(message: String, code: String = "PAYLOAD_TOO_LARGE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.URI_TOO_LONG)
open class UriTooLong(message: String, code: String = "URI_TOO_LONG", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
open class UnsupportedMediaType(message: String, code: String = "UNSUPPORTED_MEDIA_TYPE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
open class ExpectationFailed(message: String, code: String = "EXPECTATION_FAILED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
open class IAmATeapot(message: String, code: String = "I_AM_A_TEAPOT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
open class UnprocessableEntity(message: String, code: String = "UNPROCESSABLE_ENTITY", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.LOCKED)
open class Locked(message: String, code: String = "LOCKED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
open class FailedDependency(message: String, code: String = "FAILED_DEPENDENCY", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.TOO_EARLY)
open class TooEarly(message: String, code: String = "TOO_EARLY", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UPGRADE_REQUIRED)
open class UpgradeRequired(message: String, code: String = "UPGRADE_REQUIRED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
open class PreconditionRequired(message: String, code: String = "PRECONDITION_REQUIRED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
open class TooManyRequests(message: String, code: String = "TOO_MANY_REQUESTS", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE)
open class RequestHeaderFieldsTooLarge(message: String, code: String = "REQUEST_HEADER_FIELDS_TOO_LARGE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
open class UnavailableForLegalReasons(message: String, code: String = "UNAVAILABLE_FOR_LEGAL_REASONS", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
open class InternalServerError(message: String, code: String = "INTERNAL_SERVER_ERROR", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
open class NotImplemented(message: String, code: String = "NOT_IMPLEMENTED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.BAD_GATEWAY)
open class BadGateway(message: String, code: String = "BAD_GATEWAY", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
open class ServiceUnavailable(message: String, code: String = "SERVICE_UNAVAILABLE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
open class GatewayTimeout(message: String, code: String = "GATEWAY_TIMEOUT", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.HTTP_VERSION_NOT_SUPPORTED)
open class HttpVersionNotSupported(message: String, code: String = "HTTP_VERSION_NOT_SUPPORTED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES)
open class VariantAlsoNegotiates(message: String, code: String = "VARIANT_ALSO_NEGOTIATES", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
open class InsufficientStorage(message: String, code: String = "INSUFFICIENT_STORAGE", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.LOOP_DETECTED)
open class LoopDetection(message: String, code: String = "LOOP_DETECTED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
open class BandwidthLimitExceeded(message: String, code: String = "BANDWIDTH_LIMIT_EXCEEDED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

@ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
open class NetworkAuthenticationRequired(message: String, code: String = "NETWORK_AUTHENTICATION_REQUIRED", data: HashMap<*, *>? = null) :
    ApiException(message, code, data)

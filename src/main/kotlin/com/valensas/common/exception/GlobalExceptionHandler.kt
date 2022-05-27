package com.valensas.common.exception

import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes?,
    resources: Resources?,
    applicationContext: ApplicationContext?,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {
    init {
        this.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all()) { formatErrorResponse(it) }
    }

    private fun formatErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val errorAttributesMap: Map<String, Any> =
            getErrorAttributes(
                request,
                ErrorAttributeOptions.of(
                    ErrorAttributeOptions.Include.MESSAGE
                )
            )
        val status = errorAttributesMap["status"] as? Int ?: 500
        val attributes = errorAttributesMap.filterKeys { it != "path" }
        return ServerResponse
            .status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(attributes))
    }
}

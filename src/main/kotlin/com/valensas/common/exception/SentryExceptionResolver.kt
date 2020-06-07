package com.valensas.common.exception

import feign.FeignException
import io.sentry.Sentry
import org.springframework.core.Ordered
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import reactor.core.publisher.Mono
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class SentryExceptionResolver {
    private val feignInClasspath = classExists("feign.FeignException")
    private val restTemplateInClasspath = classExists("org.springframework.web.client.HttpStatusCodeException")
    private val webClientInClasspath = classExists("org.springframework.web.reactive.function.client.WebClientResponseException")

    fun shouldReport(ex: Throwable): Boolean {
        if (ex is ApiException)
            return ex.statusCode.is5xxServerError

        if (feignInClasspath && ex is FeignException)
            return ex.status() == 0 || ex.status() > 499

        if (restTemplateInClasspath && ex is HttpStatusCodeException)
            return ex.statusCode.is5xxServerError

        if (webClientInClasspath && ex is WebClientResponseException)
            return ex.statusCode.is5xxServerError

        return true
    }

    private fun classExists(name: String) = try {
        Class.forName(name, false, javaClass.classLoader) != null
    } catch (e: ClassNotFoundException) {
        false
    }
}

class ServletSentryExceptionResolver : SentryExceptionResolver(), HandlerExceptionResolver, Ordered {
    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        if (shouldReport(ex)) {
            Sentry.capture(ex)
        }
        return null
    }

    override fun getOrder(): Int = Int.MIN_VALUE
}

class ReactiveSentryExceptionResolver : SentryExceptionResolver(), WebExceptionHandler, Ordered {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> =
        Mono.defer {
            if (shouldReport(ex)) {
                Sentry.capture(ex)
            }
            Mono.empty<Void>()
        }

    override fun getOrder(): Int = Int.MIN_VALUE
}

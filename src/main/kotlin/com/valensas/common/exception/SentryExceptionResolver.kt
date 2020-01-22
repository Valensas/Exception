package com.valensas.common.exception

import feign.FeignException
import io.sentry.Sentry
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import reactor.core.publisher.Mono

abstract class SentryExceptionResolver {
    fun shouldReport(ex: Throwable): Boolean {
        return when (ex) {
            is ApiException -> ex.statusCode.is5xxServerError
            is FeignException -> ex.status() == 0 || ex.status() > 499
            is HttpStatusCodeException -> ex.statusCode.is5xxServerError
            else -> true
        }
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

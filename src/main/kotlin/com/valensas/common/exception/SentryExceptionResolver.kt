package com.valensas.common.exception

import feign.FeignException
import io.sentry.Sentry
import org.springframework.core.Ordered
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SentryExceptionResolver: HandlerExceptionResolver, Ordered {
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

    private fun shouldReport(ex: Exception): Boolean {
        return when (ex) {
            is ApiException -> ex.statusCode.is5xxServerError
            is FeignException -> ex.status() == 0 || ex.status() > 499
            is HttpStatusCodeException -> ex.statusCode.is5xxServerError
            else -> true
        }
    }

    override fun getOrder(): Int = Int.MIN_VALUE
}

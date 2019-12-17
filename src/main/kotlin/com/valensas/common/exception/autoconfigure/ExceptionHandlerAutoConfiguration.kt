package com.valensas.common.exception.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.SentryExceptionResolver
import com.valensas.common.exception.handler.ApiExceptionErrorHandler
import com.valensas.common.exception.handler.FeignErrorHandler
import com.valensas.common.exception.handler.RestTemplateErrorHandler
import io.sentry.spring.SentryServletContextInitializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy
class ExceptionHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun apiExceptionErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): ApiExceptionErrorHandler {
        return ApiExceptionErrorHandler(
            mapper = mapper,
            debug = debugProperties.debug.enabled,
            debugPackages = debugProperties.debug.packages,
            log4xx = debugProperties.logger.log4xx,
            log5xx = debugProperties.logger.log5xx
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun restTemplateErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): RestTemplateErrorHandler {
        return RestTemplateErrorHandler(
            mapper = mapper,
            debug = debugProperties.debug.enabled,
            debugPackages = debugProperties.debug.packages,
            log4xx = debugProperties.logger.log4xx,
            log5xx = debugProperties.logger.log5xx
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun feignErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): FeignErrorHandler {
        return FeignErrorHandler(
            mapper = mapper,
            debug = debugProperties.debug.enabled,
            debugPackages = debugProperties.debug.packages,
            log4xx = debugProperties.logger.log4xx,
            log5xx = debugProperties.logger.log5xx
        )
    }

    @Bean
    fun sentryExceptionResolver(): SentryExceptionResolver {
        return SentryExceptionResolver()
    }

    @Bean
    fun sentryServletContextInitializer(): ServletContextInitializer {
        return SentryServletContextInitializer()
    }
}

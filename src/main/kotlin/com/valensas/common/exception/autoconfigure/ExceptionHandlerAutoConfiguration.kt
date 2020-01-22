package com.valensas.common.exception.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ReactiveSentryExceptionResolver
import com.valensas.common.exception.SentryExceptionResolver
import com.valensas.common.exception.ServletSentryExceptionResolver
import com.valensas.common.exception.handler.ApiExceptionErrorHandler
import com.valensas.common.exception.handler.FeignErrorHandler
import com.valensas.common.exception.handler.RestTemplateErrorHandler
import com.valensas.common.exception.handler.WebClientErrorHandler
import feign.Feign
import io.sentry.spring.SentryServletContextInitializer
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ApiExceptionHandlerAutoConfiguration {
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
}

@Configuration
@ConditionalOnClass(Feign::class)
class FeignErrorHandlerAutoConfiguration {
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
}

@Configuration
@ConditionalOnClass(WebClient::class)
class WebClientErrorHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun webClientErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): WebClientErrorHandler {
        return WebClientErrorHandler(
            mapper = mapper,
            debug = debugProperties.debug.enabled,
            debugPackages = debugProperties.debug.packages,
            log4xx = debugProperties.logger.log4xx,
            log5xx = debugProperties.logger.log5xx
        )
    }
}

@Configuration
@ConditionalOnClass(RestTemplate::class)
class RestTemplateErrorHandlerAutoConfiguration {
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
}

@Configuration
class SentryAutoConfiguration {
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    fun servletSentryExceptionResolver(): SentryExceptionResolver {
        return ServletSentryExceptionResolver()
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    fun reactiveSentryExceptionResolver(): SentryExceptionResolver {
        return ReactiveSentryExceptionResolver()
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    fun sentryServletContextInitializer(): ServletContextInitializer {
        return SentryServletContextInitializer()
    }
}

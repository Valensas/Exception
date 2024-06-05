package com.valensas.exception.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.exception.ApiException
import com.valensas.exception.ParameterException
import com.valensas.exception.handler.ApiExceptionErrorHandler
import com.valensas.exception.handler.FeignErrorHandler
import com.valensas.exception.handler.RestTemplateErrorHandler
import com.valensas.exception.handler.WebClientErrorHandler
import com.valensas.exception.handler.WebFluxParameterExceptionErrorHandler
import com.valensas.exception.handler.WebParameterExceptionErrorHandler
import feign.Feign
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@RegisterReflectionForBinding(
    ApiException::class
)
class ApiExceptionHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun apiExceptionErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): ApiExceptionErrorHandler {
        return ApiExceptionErrorHandler(
            mapper = mapper,
            debugProperties = debugProperties
        )
    }
}

@Configuration
@RegisterReflectionForBinding(
    ParameterException::class
)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class WebFluxParameterExceptionHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun webFluxParameterExceptionErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): WebFluxParameterExceptionErrorHandler {
        return WebFluxParameterExceptionErrorHandler(
            mapper = mapper,
            debugProperties = debugProperties
        )
    }
}

@Configuration
@RegisterReflectionForBinding(
    ParameterException::class
)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class WebParameterExceptionErrorHandlerConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun webParameterExceptionErrorHandler(
        mapper: ObjectMapper,
        debugProperties: ExceptionHandlerConfigurationProperties
    ): WebParameterExceptionErrorHandler {
        return WebParameterExceptionErrorHandler(
            mapper,
            debugProperties
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
            debugProperties = debugProperties
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
            debugProperties = debugProperties
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
            debugProperties = debugProperties
        )
    }
}

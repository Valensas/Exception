package com.valensas.common.exception.autoconfigure

import com.valensas.common.exception.GlobalExceptionHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer

@Configuration
class GlobalExceptionHandlerAutoConfiguration {
    @Bean
    @Order(-2)
    @ConditionalOnMissingBean
    fun globalExceptionHandler(
        errorAttributes: ErrorAttributes?,
        resources: Resources?,
        applicationContext: ApplicationContext?,
        serverCodeConfigurer: ServerCodecConfigurer
    ): GlobalExceptionHandler {
        return GlobalExceptionHandler(errorAttributes, resources, applicationContext, serverCodeConfigurer)
    }

    @Bean
    @ConditionalOnMissingBean
    fun webPropertiesResources(): Resources {
        return Resources()
    }
}

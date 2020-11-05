package com.valensas.common.exception.autoconfigure

import com.valensas.common.exception.GlobalExceptionHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.ResourceProperties
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
        resourceProperties: ResourceProperties?,
        applicationContext: ApplicationContext?,
        serverCodecConfigurer: ServerCodecConfigurer
    ): GlobalExceptionHandler {
        return GlobalExceptionHandler(errorAttributes, resourceProperties, applicationContext, serverCodecConfigurer)
    }
}

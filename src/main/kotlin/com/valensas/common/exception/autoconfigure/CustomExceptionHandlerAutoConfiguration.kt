package com.valensas.common.exception.autoconfigure

import com.valensas.common.exception.CustomErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class CustomExceptionHandlerAutoConfiguration {
    @Bean
    @Primary
    fun customErrorAttributes(): CustomErrorAttributes {
        return CustomErrorAttributes()
    }
}

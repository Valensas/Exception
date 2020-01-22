package com.valensas.common.exception.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("valensas.exception")
class ExceptionHandlerConfigurationProperties {
    @Component
    @ConfigurationProperties("logger")
    class LoggerConfigurationProperties {
        var log4xx: Boolean = false
        var log5xx: Boolean = true
    }

    @Component
    @ConfigurationProperties("debug")
    class DebugConfigurationProperties {
        var enabled: Boolean = false
        var packages: List<String> = emptyList()
    }

    var debug = DebugConfigurationProperties()
    var logger = LoggerConfigurationProperties()
}

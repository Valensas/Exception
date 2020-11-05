package com.valensas.common.exception

import org.slf4j.LoggerFactory
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

class CustomErrorAttributes() : DefaultErrorAttributes() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val errorAttributes = super.getErrorAttributes(request, options)
        logger.info("Error attributes: {}", options)
        logger.info("Request attributes: {}", request)
        when (val error = getError(request)) {
            is ApiException -> {
                errorAttributes["code"] = error.code
                errorAttributes["data"] = error.data
                errorAttributes["class"] = error.`class` ?: error.javaClass.canonicalName
                error.message?.let { errorAttributes["message"] = it }
                errorAttributes["statusCode"] = error.statusCode.value()
                errorAttributes["description"] = "API Exception"
            }
            is ResponseStatusException -> errorAttributes["description"] = "Response Exception"
        }
        return errorAttributes
    }
}

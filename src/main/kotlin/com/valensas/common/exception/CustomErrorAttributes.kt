package com.valensas.common.exception

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

class CustomErrorAttributes(private val objectMapper: ObjectMapper) : DefaultErrorAttributes() {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${spring.application.name:}")
    private lateinit var serviceName: String

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
            is ResponseStatusException -> {
                errorAttributes["description"] = "Response Exception"
                errorAttributes["service"] = serviceName
            }
            is WebClientResponseException -> {
                errorAttributes["error"] = error.statusCode.reasonPhrase
                errorAttributes["status"] = error.statusCode.value()
                val jsonMessage: JsonNode? = try {
                    objectMapper.readTree(error.responseBodyAsString)
                } catch (ex: Exception) {
                    null
                }
                errorAttributes["message"] = jsonMessage?.get("message")?.textValue() ?: error.message
                errorAttributes["service"] = jsonMessage?.get("service")?.textValue() ?: serviceName
                errorAttributes["description"] = "WebClient Response Exception"
            }
            else -> errorAttributes["service"] = serviceName
        }
        return errorAttributes
    }
}

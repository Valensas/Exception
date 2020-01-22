package com.valensas.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.valensas.common.exception.ApiException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

abstract class HttpErrorHandler(
    protected val mapper: ObjectMapper,
    private val defaultHeaders: HttpHeaders = HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON },
    private val log4xx: Boolean,
    private val log5xx: Boolean
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun responseWith(body: Any?, statusCode: HttpStatus): ResponseEntity<Any> {
        return ResponseEntity(body, defaultHeaders, statusCode).also {
            when {
                it.statusCodeValue in 400..500 && log4xx -> logger.error("Returning {}, body: {}", it.statusCodeValue, it.body)
                it.statusCodeValue in 500..600 && log5xx -> logger.error("Returning {}, body: {}", it.statusCodeValue, it.body)
            }
        }
    }

    private fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
        mutableMapOf<K, V>().apply { for ((k, v) in this@filterNotNullValues) if (v != null) put(k, v) }

    protected fun convert(exception: ApiException, cause: Throwable?, debug: Boolean, debugPackages: List<String>): Any {
        val body: MutableMap<String, Any?> = mapOf(
            "code" to exception.code,
            "message" to exception.message,
            "data" to exception.data
        ).filterNotNullValues().toMutableMap()
        if (debug) {
            body.putIfAbsent("class", exception.`class` ?: exception.javaClass.canonicalName)
            val trace = exception.trace?.toMutableList() ?: mutableListOf()
            val lineMatchingPackages = (cause ?: exception)
                .stackTrace
                .firstOrNull { traceLine -> debugPackages.any { traceLine.className.startsWith(it) } }

            val firstLine = exception.stackTrace.firstOrNull()
            val currentTrace = lineMatchingPackages?.toString() ?: firstLine?.toString() ?: "unknown"

            trace.add(currentTrace)
            body.putIfAbsent("trace", trace)
        }
        return body
    }
}

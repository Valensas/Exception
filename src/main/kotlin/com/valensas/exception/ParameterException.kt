package com.valensas.exception

open class ParameterException(
    val id: String? = null,
    val path: String? = null,
    val message: String,
    val parameter: String? = null,
    val code: ParameterErrorCode,
    val fieldErrors: List<FieldError>? = null
)

data class FieldError(
    val field: String?,
    val message: String?
)

enum class ParameterErrorCode {
    Missing,
    Invalid
}

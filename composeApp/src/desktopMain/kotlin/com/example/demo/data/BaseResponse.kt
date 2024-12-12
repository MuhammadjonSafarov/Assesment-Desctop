package com.example.demo.data

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val state: String,
    val status: String,
    val statusCode: Long,
    val message: String?,
    val data:T
)
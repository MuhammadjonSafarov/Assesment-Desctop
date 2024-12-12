package com.example.demo.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Long,
    val token: String,
    val username: String,
    val role: Array<String?>,
    val permissions: Array<String>?
)
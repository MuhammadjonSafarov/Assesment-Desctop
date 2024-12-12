package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class FileDto(
    val absolutePath: String,
    val contentType: String,
    val id: Int,
    val name: String,
    val size: Long,
    val url: String
)
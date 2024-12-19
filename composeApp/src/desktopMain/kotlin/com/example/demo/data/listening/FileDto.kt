package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class FileDto(
    val absolutePath: String="",
    val contentType: String="",
    val id: Long=0,
    val name: String="",
    val size: Long = 0,
    val url: String=""
)
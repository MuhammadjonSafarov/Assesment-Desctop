package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class ListeningData(
    val audioIds: Array<Long>?,
    val audios: List<Audio>,
    val id: Int,
    val name: String
)
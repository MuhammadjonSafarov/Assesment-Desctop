package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class AnswerData(
    val answer: String,
    val id: Long,
    val isCorrect: Boolean
)
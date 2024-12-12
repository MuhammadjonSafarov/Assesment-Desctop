package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class QuestionData(
    val answerIds: Long?,
    val answers: List<Long>?,
    val correctAnswersCount: Int,
    val id: Int,
    val name: String
)
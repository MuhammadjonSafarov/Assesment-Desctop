package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class QuestionData(
    val answerIds: Long?,
    val answers: List<AnswerData>?,
    val correctAnswersCount: Int,
    val id: Long,
    val name: String
)
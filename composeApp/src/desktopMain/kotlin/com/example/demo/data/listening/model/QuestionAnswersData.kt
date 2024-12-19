package com.example.demo.data.listening.model

import com.example.demo.data.listening.AnswerData

data class QuestionAnswersData(
    val id:Long,
    val name: String,
    val answerIds: List<Long>,
    val answers: List<AnswerData>,
    val correctAnswersCount: Int
):ListeningParent()
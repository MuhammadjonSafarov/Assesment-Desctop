package com.example.demo.data.listening

import kotlinx.serialization.Serializable

@Serializable
data class Audio(
    val audiFileDto: FileDto,
    val audioFileId: Long?,
    val fileDtos: List<FileDto>?,
    val fileIds: Array<Long>?,
    val id: Long,
    val name: String,
    val orderNumber: Int,
    val questionIds: Array<Long>?,
    val questions: List<QuestionData>
)
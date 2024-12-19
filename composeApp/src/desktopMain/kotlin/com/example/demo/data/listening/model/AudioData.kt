package com.example.demo.data.listening.model

import com.example.demo.data.listening.FileDto

data class AudioData(
    val id:Long,
    val name:String,
    val audioFile: FileDto,
    val images:List<FileDto>
):ListeningParent()
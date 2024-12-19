package com.example.demo.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.example.demo.data.listening.Audio
import com.example.demo.data.listening.FileDto
import com.example.demo.data.listening.model.AudioData
import com.example.demo.data.listening.model.ListeningParent
import com.example.demo.data.listening.model.QuestionAnswersData
import com.github.sarxos.webcam.Webcam
import javazoom.jl.player.advanced.AdvancedPlayer
import javazoom.jl.player.advanced.PlaybackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import org.jetbrains.skia.Bitmap
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream

fun getCacheDir(): File {
    val os = System.getProperty("os.name").lowercase()

    return when {
        os.contains("win") -> File(System.getenv("LOCALAPPDATA"), "Assesment/Cache")
        os.contains("mac") -> File(System.getProperty("user.home"), "Library/Caches/MyApp")
        else -> File(System.getProperty("user.home"), ".myapp/cache")
    }
}


suspend fun convertData(audiosList: List<Audio?>): List<ListeningParent> = withContext(Dispatchers.IO) {
    val mutableListening = mutableListOf<ListeningParent>()
    audiosList.forEach { audio ->
        val audioData = AudioData(
            id = audio?.id ?: 0,
            name = audio?.name ?: "",
            audioFile = audio?.audiFileDto ?: FileDto(),
            images = audio?.fileDtos ?: emptyList()
        )
        mutableListening.add(audioData)
        audio?.questions?.forEach { questionData ->
            val questionAnswersData = QuestionAnswersData(
                id = questionData.id,
                name = questionData.name,
                answerIds = listOf(),
                answers = questionData.answers ?: emptyList(),
                correctAnswersCount = 0
            )
            mutableListening.add(questionAnswersData)
        }
    }
    delay(1000)
    mutableListening
}

suspend fun playSound(imagesSize:Int,
                      audioFile:String,
                      onProgress: (Float, Int,Int) -> Unit,
                      maxDuration:(Int) -> Unit
) = withContext(Dispatchers.IO) {
    var progress :Float = 0f
    var elapsedTime :Int = 0
    var imageCounter :Int = 0
    val file = File(audioFile)
    val fis = FileInputStream(file)
    val totalBytes = fis.available().toFloat()
    val bitrate = 128_000 // Example bitrate: 128 kbps
    val duration = ((totalBytes * 8) / bitrate).toInt()
    maxDuration.invoke(duration)
    val player = AdvancedPlayer(fis)
    launch(Dispatchers.Default){
        while (progress < 1f) {
            val playedBytes = totalBytes - fis.available()
            progress = playedBytes / totalBytes
            elapsedTime = (progress * duration).toInt()
            if (elapsedTime > ((imageCounter + 1) * (duration / (imagesSize-1)))) {
                imageCounter++
            }
            delay(200) // Update progress every 200ms
            onProgress.invoke(progress,elapsedTime,imageCounter)
        }
    }
    player.play()
}


suspend fun cameraScreenPlay(webcam: Webcam,recorder: FFmpegFrameRecorder,
                         onBitmap:(ImageBitmap) -> Unit) = withContext(Dispatchers.IO){
    val converter = Java2DFrameConverter()
    val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < 10000) {  // 10000 millisekund = 10 soniya
            val image: BufferedImage = webcam.image
            val frame: Frame = converter.convert(image)
            val bitmap = image.toComposeImageBitmap()
            onBitmap(bitmap)
            recorder.record(frame)
        }
  }
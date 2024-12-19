package com.example.demo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import cafe.adriel.voyager.core.screen.Screen
import com.example.demo.util.cameraScreenPlay
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import kotlinx.coroutines.*
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage

class DetailScreen : Screen {
    @Composable
    override fun Content() {
        var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val scope = rememberCoroutineScope()
        val webcam = Webcam.getDefault()
        webcam.setViewSize(WebcamResolution.VGA.size)
        webcam.open()


        val recorder = FFmpegFrameRecorder(
            "output_video.mp4",
            webcam.viewSize.width,
            webcam.viewSize.height)
        recorder.format = "mp4"
        recorder.videoCodec = org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264
        recorder.frameRate = 30.0 // Frame rate - 30 FPS
        recorder.start()

        LaunchedEffect(Unit){
            scope.launch{
                cameraScreenPlay(webcam,recorder,
                    onBitmap = { bitmap = it }
                )
            }
        }

        bitmap?.let {
            Image(bitmap = it, contentDescription = "Front Camera", modifier = Modifier.fillMaxSize())
        }

        DisposableEffect(Unit){
            onDispose {
                recorder.stop()
                recorder.release()
                webcam.close()
                println("Video saqlandi: output_video.mp4")
            }
        }
    }
}

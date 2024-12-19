package com.example.demo.screen

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import javazoom.jl.decoder.Bitstream
import javazoom.jl.decoder.Header
import javazoom.jl.player.advanced.AdvancedPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.InputStream

class MusicPlayerScreen:Screen {

    @Composable
    override fun Content() {
        var player: AdvancedPlayer? by remember { mutableStateOf(null) }
        var isPlaying by remember { mutableStateOf(false) }
        var isPaused by remember { mutableStateOf(false) }
        var job: Job? by remember { mutableStateOf(null) }
        var currentFrame by remember { mutableStateOf(0) } // Joriy frame saqlanadi
        val filePath = "music.mp3" // Fayl yo'lini to'g'rilang

        fun getFrameCount(inputStream: InputStream): Int {
            val bitstream = Bitstream(inputStream)
            var count = 0
            try {
                while (true) {
                    val header: Header = bitstream.readFrame() ?: break
                    count++
                    bitstream.closeFrame()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return count
        }

        fun playMusic(fromFrame: Int = 0) {
            job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    FileInputStream(filePath).use { fileStream ->
                        val frameCount = getFrameCount(FileInputStream(filePath))
                        player = AdvancedPlayer(fileStream)
                        for (i in 0 until fromFrame) {
                            player?.play(1) // Oldingi framelarni o'tkazib yuborish
                        }
                        isPlaying = true
                        isPaused = false
                        player?.play(frameCount - fromFrame)
                    }
                    isPlaying = false
                    player = null
                } catch (e: Exception) {
                    println("Error playing music: ${e.message}")
                }
            }
        }

        fun pauseMusic() {
            player?.close() // AdvancedPlayer'ni yopamiz
            job?.cancel()   // Coroutine'ni bekor qilamiz
            isPaused = true
            isPlaying = false
        }

        // UI: Play/Pause tugmasi
        Button(onClick = {
            if (!isPlaying) {
                playMusic(currentFrame) // Saqlangan joydan davom ettirish
            } else {
                pauseMusic()
            }
        }) {
            Text(if (isPlaying) "Pause" else "Play")
        }
    }
}
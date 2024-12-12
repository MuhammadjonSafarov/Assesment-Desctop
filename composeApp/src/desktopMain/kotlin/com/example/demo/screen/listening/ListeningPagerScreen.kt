package com.example.demo.screen.listening

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.demo.api.ApiService
import com.example.demo.data.listening.Audio
import com.example.demo.util.AppColors
import com.example.demo.util.getCacheDir
import com.example.demo.util.loadPicture
import javazoom.jl.player.advanced.AdvancedPlayer
import kotlinx.coroutines.*
import java.io.File
import java.io.FileInputStream
import java.util.prefs.Preferences

class MyPagerScreen : Screen {
    @Composable
    override fun Content() {
        val pages = listOf(
            "Sahifa 1: Bismillah...",
            "Sahifa 2: Ilk qadam",
            "Sahifa 3: To'xtovsiz o'qish",
            "Sahifa 4: To'xtovsiz o'qish"
        )
        val preference = Preferences.userRoot().node("my_app")
        val token = preference.get("key_user_token", "")
        var currentPage by remember { mutableStateOf(0) }
        var audiosList by remember { mutableStateOf<List<Audio?>>(listOf()) }
        var timeRemaining by remember { mutableStateOf(600) }  // 10 minut = 600 sekund
        var isRunning by remember { mutableStateOf(true) } // Timerni ishga tushirish holati
        var isLoading by remember { mutableStateOf(false) } // Timerni ishga tushirish holati
        val scope = rememberCoroutineScope()
        val minutes = timeRemaining / 60
        val seconds = timeRemaining % 60
        if (audiosList.isNotEmpty()){
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f)
                        .background(color = AppColors.textColorBlue),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOEFL",
                        modifier = Modifier.align(Alignment.CenterVertically)
                            .padding(start = 24.dp, end = 32.dp),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterVertically)
                            .padding(start = 12.dp, end = 12.dp)
                    ) {
                        Button(
                            modifier = Modifier.padding(all = 6.dp),
                            onClick = { if (currentPage > 0) currentPage-- },
                            enabled = currentPage > 0,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColors.textColorBlue, contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.elevation(0.dp),
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 16.dp, top = 4.dp, bottom = 4.dp, end = 16.dp
                                ), text = "Volume"
                            )
                        }
                        Button(
                            modifier = Modifier.padding(all = 6.dp),
                            onClick = { if (currentPage < pages.size - 1) currentPage++ },
                            enabled = currentPage < pages.size - 1,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColors.textColorBlue, contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.elevation(0.dp),
                            border = BorderStroke(1.dp, Color.White)
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 16.dp, top = 4.dp, bottom = 4.dp, end = 16.dp
                                ), text = "Next"
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Listening |", modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                    )

                    Text(
                        "Time Remaining: $minutes:${seconds.toString().padStart(2, '0')}",
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                    )

                }
                Pager(
                    pageCount = pages.size,
                    currentPage = currentPage,
                    modifier = Modifier.weight(1f)
                ) { pageIndex ->
                    if (pageIndex == 0) VoicePage(audiosList[0])
                    else BookPage(pages)
                }
            }
        } else {
            Box (modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        LaunchedEffect(isRunning) {
            while (isRunning && timeRemaining > 0) {
                delay(1_000L)  // Har 1 sekund kutish
                timeRemaining -= 1  // Sekundni kamaytirish
            }
        }
        LaunchedEffect(Unit) {
            scope.launch {
                isLoading = true
                val result = ApiService().getListening(0, token)
                audiosList = result?.audios?: emptyList()
                isLoading = false
            }
        }
    }

    @Composable
    fun BookPage(items: List<String>) {
        var selectedOption by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Yerning sakli qanaqa ...",fontSize = 18.sp)
            LazyColumn(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    Row {
                        RadioButton(enabled = true, selected = (selectedOption == item), onClick = {
                            selectedOption = item
                        })
                        Text(modifier = Modifier.align(Alignment.CenterVertically), text = item)
                    }

                }
            }
        }

    }

    @Composable
    fun VoicePage(audio: Audio?) {
        var isPlaying by remember { mutableStateOf(false) }
        var audioFile by remember { mutableStateOf("") }
        var progress by remember { mutableStateOf(0f) } // Progress (0.0 - 1.0)
        var duration by remember { mutableStateOf(0) } // Total duration in seconds
        var elapsedTime by remember { mutableStateOf(0) } // Elapsed time in seconds
        val coroutineScope = rememberCoroutineScope()
        var imageCounter by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bitmap = useResource("no_image.png") {
                loadImageBitmap(it)
            }
            Image(
                bitmap = loadPicture("https://test.assessmenttest.uz/images/${audio?.fileDtos?.get(imageCounter%(audio.fileDtos.size))?.name?:""}"),
                "news thumbnail ",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(0.6f)
            )
            Text("Progress: ${String.format("%.2f", progress * 100)}%", modifier = Modifier.padding(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(0.6f)
                    .padding(start = 32.dp, end = 32.dp),
                color = AppColors.textColorBlue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
            ) {
                Button(onClick = {
                    isPlaying = true
                    imageCounter++
                }, enabled = !isPlaying) {
                    Text("Play")
                }

                Button(onClick = {
                    coroutineScope.coroutineContext.cancelChildren()
                    isPlaying = false
                    progress = 0f
                    elapsedTime = 0
                }, enabled = isPlaying) {
                    Text("Stop")
                }
            }
            Text("Elapsed Time: ${elapsedTime}s / ${duration}s", modifier = Modifier.padding(16.dp))
        }
        var player: AdvancedPlayer? = null
        if (isPlaying) {
            LaunchedEffect(Unit) {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val file = File(audioFile)
                        val fis = FileInputStream(file)
                        val totalBytes = fis.available().toFloat()
                        val bitrate = 128_000 // Example bitrate: 128 kbps
                        duration = ((totalBytes * 8) / bitrate).toInt()
                        player = AdvancedPlayer(fis)
                        launch(Dispatchers.Default) {
                            while (isPlaying && progress < 1f) {
                                val playedBytes = totalBytes - fis.available()
                                progress = playedBytes / totalBytes
                                elapsedTime = (progress * duration).toInt()
                                delay(200) // Update progress every 200ms
                            }
                        }

                        player?.play() // Blocking method
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        isPlaying = false
                        progress = 0f
                        elapsedTime = 0
                    }
                }
            }
        } else {
            player?.close()
        }
        LaunchedEffect(Unit){
          coroutineScope.launch {
              getCacheDir().mkdirs()
              val filePath = "${getCacheDir()}/${audio?.audiFileDto?.name}"
              ApiService().downloadMusic("https://test.assessmenttest.uz/images/${audio?.audiFileDto?.name}",filePath)
              audioFile = filePath
          }
        }
    }

}

@Composable
fun Pager(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    content: @Composable (pageIndex: Int) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()
        .pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            change.consume()
            if (dragAmount > 100) { // Chapdan o'ngga
                // currentPage = (currentPage - 1).coerceAtLeast(0)
            } else if (dragAmount < -100) { // O'ngdan chapga
                //currentPage = (currentPage + 1).coerceAtMost(pageCount - 1)
            }
        }
    }) {
        repeat(pageCount) { pageIndex ->
            if (pageIndex == currentPage) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    content(pageIndex)
                }
            }
        }
    }
}
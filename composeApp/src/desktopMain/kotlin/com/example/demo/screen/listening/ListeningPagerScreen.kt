package com.example.demo.screen.listening

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.demo.api.ApiService
import com.example.demo.data.listening.model.AudioData
import com.example.demo.data.listening.model.ListeningParent
import com.example.demo.data.listening.model.QuestionAnswersData
import com.example.demo.data.listening.model.SelectQuestionAnswersData
import com.example.demo.util.*
import kotlinx.coroutines.*
import java.util.prefs.Preferences

class MyPagerScreen : Screen {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Xatolik: ${throwable.localizedMessage}")
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val preference = Preferences.userRoot().node("my_app")
        val token = preference.get("key_user_token", "")
        var currentPage by remember { mutableStateOf(0) }
        var timer by remember { mutableStateOf(600) }
        var showDialog by remember { mutableStateOf(false) }
        var showNexButton by remember { mutableStateOf(false) }
        var audiosList by remember { mutableStateOf<List<ListeningParent>>(listOf()) }
        val answersList by remember { mutableStateOf<MutableList<SelectQuestionAnswersData>>(mutableListOf()) }
        var isTimeRunning by remember { mutableStateOf(false) } // Timerni ishga tushirish holati
        var isLoading by remember { mutableStateOf(false) } // Timerni ishga tushirish holati
        val scope = CoroutineScope(Dispatchers.Main + exceptionHandler)

        if (isTimeRunning){
            LaunchedEffect(Unit) {
                scope.launch {
                    while (isTimeRunning && timer > 0) {
                        delay(1000)
                        timer --
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            scope.launch {
                isLoading = true
                val response = ApiService().getListening(0, token)
                audiosList = convertData(response?.audios ?: listOf())
                isLoading = false
                isTimeRunning = true
            }
        }
        val minutes = timer / 60
        val seconds = timer % 60
        if (audiosList.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f).background(color = AppColors.textColorBlue),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOEFL",
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 24.dp, end = 32.dp),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterVertically).padding(start = 12.dp, end = 12.dp)
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
                        if (showNexButton)
                        Button(
                            modifier = Modifier.padding(all = 6.dp),
                            onClick = { currentPage ++ },
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
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                    pageIndex = currentPage,
                    modifier = Modifier.weight(1f),
                    audiosList,
                    onNextButton = {
                        showNexButton = it
                    },
                    scope
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        val timerName = "minutes:${seconds.toString().padStart(2, '0')}"
        if (showDialog){
            navigator.pop()
            navigator.push(ListeningFinishScreen(timerName,answersList))
        }
    }
    @Composable
    fun Pager(
        pageIndex: Int,
        modifier: Modifier = Modifier,
        audiosList: List<ListeningParent>,
        onNextButton:(Boolean)-> Unit,
        scope:CoroutineScope
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            if (pageIndex < audiosList.size){
                onNextButton(false)
                if (audiosList[pageIndex] is AudioData) {
                    VoicePage(scope,audiosList[pageIndex] as AudioData,
                        onFinishSound = {
                            println("onFinishSound")
                            onNextButton(true)
                        })
                } else if (audiosList[pageIndex] is QuestionAnswersData) {
                    BookPage(audiosList[pageIndex] as QuestionAnswersData,
                        onItemSelect = { first,second->
                            println("onItemSelect")
                            onNextButton(true)
                        })
                }
            } else {
                scope.cancel()
            }
        }
    }

    @Composable
    fun BookPage(question: QuestionAnswersData,onItemSelect:(String,String)-> Unit) {
        var selectedOption by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = question.name, fontSize = 18.sp)
            LazyColumn(
                modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(question.answers) { item ->
                    Row {
                        RadioButton(enabled = true, selected = (selectedOption == item.answer), onClick = {
                            selectedOption = item.answer
                            onItemSelect.invoke(question.name, selectedOption)
                        })
                        Text(modifier = Modifier.align(Alignment.CenterVertically), text = "${item.answer}")
                    }

                }
            }
        }

    }

    @Composable
    fun VoicePage(scope:CoroutineScope,audio: AudioData?,onFinishSound:(Unit)->Unit) {
        var isPlaying by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var audioFile by remember { mutableStateOf("") }
        var progress by remember { mutableStateOf(0f) } // Progress (0.0 - 1.0)
        var duration by remember { mutableStateOf(0) } // Total duration in seconds
        var elapsedTime by remember { mutableStateOf(0) } // Elapsed time in seconds
        var imageCounter by remember { mutableStateOf(0) }
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = loadPicture("https://test.assessmenttest.uz/images/${audio?.images?.get(imageCounter)?.name ?: ""}"),
                    "news thumbnail ",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(0.6f)
                )
                Text("Progress: ${String.format("%.2f", progress * 100)}%", modifier = Modifier.padding(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(0.6f).padding(start = 32.dp, end = 32.dp),
                    color = AppColors.textColorBlue
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Elapsed Time: ${elapsedTime}s / ${duration}s", modifier = Modifier.padding(16.dp))
            }
        }
        LaunchedEffect(Unit) {
            scope.launch {
                isLoading = true
                getCacheDir().mkdirs()
                val filePath = "${getCacheDir()}/${audio?.audioFile?.name}"
                ApiService().downloadMusic(
                    url = "https://test.assessmenttest.uz/images/${audio?.audioFile?.name}",
                    outputFile = filePath)
                withContext(Dispatchers.Main) {
                    audioFile = filePath
                    isLoading = false
                    isPlaying = true
                }
            }
        }
        if (isPlaying) {
            LaunchedEffect(Unit) {
                scope.launch {
                    try {
                        playSound(
                            imagesSize = audio?.images?.size?:0,
                            audioFile = audioFile,
                            onProgress = { first,second,three->
                                    progress = first
                                    elapsedTime = second
                                if(imageCounter != three){
                                    imageCounter = three
                                }
                                println("progress $progress, elapsedTime $elapsedTime, imageCounter $imageCounter")
                            },
                            maxDuration = {
                                duration = it
                                println("duration $duration")
                            })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        onFinishSound.invoke(Unit)
                        isPlaying = false
                        progress = 0f
                        elapsedTime = 0
                    }
                }
            }
        } else {

        }
    }

}


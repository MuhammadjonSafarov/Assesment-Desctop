package com.example.demo.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.demo.screen.listening.MyPagerScreen
import com.example.demo.util.AppColors
import com.example.demo.util.language.getStrings
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import javax.sound.sampled.*

class SelectExamScreen:Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var locale by remember { mutableStateOf(Locale("en")) }
        val strings = getStrings(locale)
        var showDialog by remember { mutableStateOf(false) }
        var isRecording by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource("logo.xml"),
                    contentDescription = "Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxWidth(0.314f).fillMaxHeight(0.24f)
                )
                Button(
                    modifier = Modifier.padding(all = 12.dp), onClick = {
                        navigator.push(MyPagerScreen())
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White, contentColor = AppColors.textColorBlue
                    ), elevation = ButtonDefaults.elevation(0.dp), border = BorderStroke(1.dp, AppColors.textColorBlue)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 24.dp, top = 4.dp, bottom = 4.dp, end = 24.dp
                        ), text = "Listining"
                    )
                    RadioButton( selected = false,
                        onClick = {
                            showDialog=true
                        }
                    )
                }
                Button(
                    modifier = Modifier.padding(all = 12.dp), onClick = {
                        // navigator.push(SelectedPlaceScreen(locale.language))
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White, contentColor = AppColors.textColorBlue
                    ), elevation = ButtonDefaults.elevation(0.dp), border = BorderStroke(1.dp, AppColors.textColorBlue)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 24.dp, top = 4.dp, bottom = 4.dp, end = 24.dp
                        ), text = "Writing"
                    )
                    RadioButton(selected = false,
                        onClick = {

                        })
                }
                Button(
                    modifier = Modifier.padding(all = 12.dp), onClick = {
                        // navigator.push(SelectedPlaceScreen(locale.language))
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White, contentColor = AppColors.textColorBlue
                    ), elevation = ButtonDefaults.elevation(0.dp), border = BorderStroke(1.dp, AppColors.textColorBlue)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 24.dp, top = 4.dp, bottom = 4.dp, end = 24.dp
                        ), text = "Listining"
                    )
                    RadioButton(selected = false,
                        onClick = {

                        }
                    )
                }
                Button(
                    modifier = Modifier.padding(all = 12.dp), onClick = {
                        // navigator.push(SelectedPlaceScreen(locale.language))
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White, contentColor = AppColors.textColorBlue
                    ), elevation = ButtonDefaults.elevation(0.dp), border = BorderStroke(1.dp, AppColors.textColorBlue)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 24.dp, top = 4.dp, bottom = 4.dp, end = 24.dp
                        ), text = "Speaking"
                    )
                    RadioButton(selected = false,
                        onClick = {

                        }
                    )
                }
            }

        }
        if (showDialog) {
            AudioRecordingDialog(
                onDismiss = { showDialog = false },
                isRecording = isRecording,
                onStartRecording = { isRecording = true },
                onStopRecording = { isRecording = false }
            )
            /*DialogWindow(onCloseRequest = { showDialog = false }, title = "Diqqat!") {
                Surface {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bu dialog oynasi.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { showDialog = false }) {
                            Text("Yopish")
                        }
                    }
                }
            }*/
        }
    }

    @Composable
    fun AudioRecordingDialog(
        onDismiss: () -> Unit,
        isRecording: Boolean,
        onStartRecording: () -> Unit,
        onStopRecording: () -> Unit
    ) {
        var audioThread by remember { mutableStateOf<Thread?>(null) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Ovoz yozish") },
            text = {
                Column {
                    Text(text = if (isRecording) "Ovoz yozilmoqda..." else "Ovoz yozishga tayyor")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (isRecording) {
                                // Ovoz yozishni to'xtatish
                                onStopRecording()
                                audioThread?.interrupt()
                            } else {
                                // Ovoz yozishni boshlash
                                onStartRecording()
                                audioThread = startAudioRecording()
                            }
                        }
                    ) {
                        Text(if (isRecording) "Yozishni to'xtatish" else "Yozishni boshlash")
                    }
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Yopish")
                }
            }
        )
    }

    // Ovoz yozishni boshlovchi funksiyani yaratamiz
    fun startAudioRecording(): Thread {
        val thread = Thread {
            try {
                val format = AudioFormat(44100f, 16, 1, true, true)
                val dataLineInfo = DataLine.Info(TargetDataLine::class.java, format)
                val targetDataLine = AudioSystem.getLine(dataLineInfo) as TargetDataLine
                targetDataLine.open(format)
                targetDataLine.start()

                val byteArrayOutputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                while (!Thread.currentThread().isInterrupted) {
                    val bytesRead = targetDataLine.read(buffer, 0, buffer.size)
                    if (bytesRead > 0) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead)
                    }
                }
                val audioBytes = byteArrayOutputStream.toByteArray()
                val audioInputStream = AudioInputStream(ByteArrayInputStream(audioBytes), format, audioBytes.size.toLong() / format.frameSize)
                val outputFile = File("recorded_audio.wav")
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile)
                targetDataLine.stop()
                targetDataLine.close()
                byteArrayOutputStream.close()

                targetDataLine.stop()
                targetDataLine.close()
                byteArrayOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        return thread
    }
}


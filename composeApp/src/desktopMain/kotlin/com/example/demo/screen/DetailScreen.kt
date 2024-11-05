package com.example.demo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import cafe.adriel.voyager.core.screen.Screen
import com.github.sarxos.webcam.Webcam
import kotlinx.coroutines.delay

data class DetailScreen(val id: Long) : Screen {
    @Composable
    override fun Content() {
        CameraView()
    }
}
@Composable
fun CameraView() {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val webcam = Webcam.getDefault()
    DisposableEffect(Unit) {
        webcam.open()
        onDispose {
            webcam.close()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val image = webcam.image
            bitmap = image.toComposeImageBitmap()
            delay(100)
        }
    }
    bitmap?.let {
        Image(bitmap = it , contentDescription = "Camera Feed", modifier = Modifier.fillMaxSize())
    }
}

/* var currentLanguage by remember { mutableStateOf("uz") }

     Column(
         modifier = androidx.compose.ui.Modifier.padding(16.dp)
     ) {
         Text(text = Localization.getString("hello"))
         Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
         Text(text = Localization.getString("goodbye"))

         Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

         Row {
             Button(onClick = {
                 currentLanguage = "en"
                 Localization.switchLocale(currentLanguage)
             }) {
                 Text("English")
             }
             Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
             Button(onClick = {
                 currentLanguage = "uz"
                 Localization.switchLocale(currentLanguage)
             }) {
                 Text("O'zbek")
             }
         }
     }*/
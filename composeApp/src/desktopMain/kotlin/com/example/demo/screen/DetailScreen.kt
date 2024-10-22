package com.example.demo.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.sarxos.webcam.Webcam
import java.awt.image.BufferedImage
import javax.swing.Timer

data class DetailScreen(val id:Long):Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        /*Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                navigator.pop()
            }){
                Text(text = "Go back (${id})")
            }
        }*/
        CameraView()
    }
}

@Composable
fun CameraView() {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    // Kamera obyektini olish
    val webcam = Webcam.getDefault()
    webcam.open()

    // Tasvir olish uchun timer
    val timer = Timer(100) {
        val bufferedImage: BufferedImage? = webcam.image
        if (bufferedImage != null) {
            image = bufferedImage.toComposeImageBitmap()
        }
    }
    timer.start()

    // UI qismi
    if (image != null) {
        Image(bitmap = image!!, contentDescription = "Camera Image", modifier = Modifier.fillMaxSize())
    } else {
        // Tasvir topilmasa yoki kamera ishlamasa
        Image(bitmap = ImageBitmap(1, 1), contentDescription = "No Camera", modifier = Modifier.fillMaxSize())
    }
}
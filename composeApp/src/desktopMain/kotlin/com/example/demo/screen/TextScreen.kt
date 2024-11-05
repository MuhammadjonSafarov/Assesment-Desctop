package com.example.demo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.voyager.core.screen.Screen
import java.util.*

class TextScreen:Screen {
    @Composable
    override fun Content() {
        var selectedLocale by remember { mutableStateOf(Locale.ENGLISH) }

        Column {
            Text(getString("app.welcome"),fontFamily = FontFamily.Serif)
            println(getString("app.welcome"))
            // Tilni tanlash
            Row {
                Button(onClick = { selectedLocale = Locale("ru") }) {
                    Text("Русский")
                }
                Button(onClick = { selectedLocale = Locale.ENGLISH }) {
                    Text("English")
                }
            }

            Button(onClick = {  }) {
                Text(getString("app.exit"),fontFamily = FontFamily.Serif)
            }
        }
    }
}

fun getString(key: String): String {
    val bundle = ResourceBundle.getBundle("strings", Locale.getDefault())
    return bundle.getString(key)
}
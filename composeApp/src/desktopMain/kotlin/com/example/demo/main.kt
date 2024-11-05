package com.example.demo

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.*
import org.jetbrains.compose.resources.painterResource
import toefl_test.composeapp.generated.resources.Res
import toefl_test.composeapp.generated.resources.avatar

fun main() = application {

    /* var secondWindowOpened by remember { mutableStateOf(true) }
     var apiResponse by remember { mutableStateOf("Waiting...") }
     var scope = rememberCoroutineScope()*/
    var tryState = rememberTrayState()
    val icon = painterResource(Res.drawable.avatar)
    if (isTraySupported) {
        Tray(
            state = tryState,
            icon = icon,
            menu = {
            Item(text = "Send notification", onClick = {
                tryState.sendNotification(
                    Notification(
                        title = "New notification",
                        message = "They here"
                    )
                )
            })
            Item(
                text = "Exit",
                onClick = ::exitApplication
            )
        })
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Toefl test",
        onKeyEvent = { it ->
        if (it.key == Key.Delete) {
            println("DELETE KEY PRESSED")
            true
        } else {
            false
        }
    }) {
        App()
    }
}

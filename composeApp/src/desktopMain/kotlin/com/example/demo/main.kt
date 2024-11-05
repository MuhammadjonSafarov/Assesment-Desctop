package com.example.demo

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.*
import demo.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

fun main() = application {

    /* var secondWindowOpened by remember { mutableStateOf(true) }
     var apiResponse by remember { mutableStateOf("Waiting...") }
     var scope = rememberCoroutineScope()*/
  /*  var tryState = rememberTrayState()
    val icon = painterResource(Res.drawable)
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
    }*/

    Window(onCloseRequest = ::exitApplication, title = "Demo", onKeyEvent = { it ->
        if (it.key == Key.Delete) {
            println("DELETE KEY PRESSED")
            true
        } else {
            false
        }
    }) {
        //CameraView()
        App()
        /*if (secondWindowOpened){
            Window(
                onCloseRequest = { secondWindowOpened = false },
                title = "New window",
                state = WindowState(width = 300.dp , height = 600.dp),
                resizable = false
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = {
                        scope.launch {
                            apiResponse = ApiService().getFetchData()
                        }
                    }){
                        Text(text = "Click Me")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = apiResponse)
                }
            }
        }*/
    }
}

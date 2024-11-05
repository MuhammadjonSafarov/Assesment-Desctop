package com.example.demo.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.demo.util.AppColors
import com.example.demo.util.getLocalIpAddress
import com.example.demo.util.getMacAddress
import com.example.demo.util.isInternetAvailable
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

class WelcomeScreen:Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isLoading by remember { mutableStateOf(false) }
        var ipAddress by remember { mutableStateOf("") }
        var macAddress by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()

        if (ipAddress.isNotEmpty() && macAddress.isNotEmpty() && isLoading) {
            navigator.push(LanguageScreen(isLoading,ipAddress,macAddress))
        } else {
            Box (modifier = Modifier.fillMaxSize()){
                Column (modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally, // Elementlarni o'rtaga keltirish
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource("logo.xml"), // Ikona resursi
                        contentDescription = "Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .fillMaxWidth(0.414f)
                            .fillMaxHeight(0.24f)
                    )
                    Spacer(modifier = Modifier
                        .padding(12.dp))
                    CircularProgressIndicator(color = AppColors.textColorBlue)
                }

            }
        }

        LaunchedEffect(Unit){
            scope.launch {
                delay(Duration.ofMillis(200))
                ipAddress = getLocalIpAddress()
                macAddress = getMacAddress()
                isLoading = isInternetAvailable()
            }
        }
    }
}
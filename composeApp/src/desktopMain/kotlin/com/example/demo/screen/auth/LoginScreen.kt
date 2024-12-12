package com.example.demo.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.demo.api.ApiService
import com.example.demo.util.AppColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.prefs.Preferences

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val preference = Preferences.userRoot().node("my_app")
        var isRememberMeChecked by remember { mutableStateOf(false) }
        var isLoginClick by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
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
                Text("Login", style = MaterialTheme.typography.h5)

                Spacer(modifier = Modifier.height(8.dp))
                // Username input
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Login") },
                    modifier = Modifier.wrapContentSize()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Parol") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.wrapContentSize()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isRememberMeChecked,
                        onCheckedChange = { isRememberMeChecked = it }
                    )
                    Text("Meni eslab qol")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isLoginClick = true
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = AppColors.textColorBlue
                    ),
                    elevation = ButtonDefaults.elevation(0.dp),
                    border = BorderStroke(1.dp, AppColors.textColorBlue)
                ) {
                    Text("Kirish")
                }
            }
            if (isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppColors.textColorBlue)
            }
        }
        if (isLoginClick){
            if (username.isNotEmpty() && password.isNotEmpty()){
                isLoading = true
                LaunchedEffect(Unit){
                    scope.launch {
                        val result = ApiService().login(username,password)
                        val token = result?.data?.token?:""
                        preference.put("key_user_token",token)
                        withContext(Dispatchers.Main){
                            isLoading = false
                        }
                    }
                }
            }else{

            }

        }
    }
}
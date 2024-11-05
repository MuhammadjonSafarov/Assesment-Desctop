package com.example.demo.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.demo.util.AppColors
import com.example.demo.util.language.getStrings
import kotlinx.coroutines.launch

class LanguageScreen(var status: Boolean, val ipAddress: String, val macAddress: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var locale by remember { mutableStateOf(Locale("en")) }
        val strings = getStrings(locale)
        var expanded by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                Button(
                    onClick = {
                        expanded = !expanded
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,     // Tugma fon rangi
                        contentColor = Color.Black       // Tugma ichidagi matn va ikona rangi
                    ), modifier = Modifier.padding(bottom = 2.dp, top = 12.dp, end = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(if (locale.language == "uz") "ic_lang_uz.xml" else if (locale.language == "ru") "ic_lang_ru.xml" else "ic_lang_en.xml"), // Ikona resursi
                        contentDescription = "Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)  // Ikona o'lchami
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Matn va ikonani ajratish uchun bo'sh joy
                    Text(if (locale.language == "uz") strings.uzbek else if (locale.language == "ru") strings.russion else strings.english)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(onClick = {
                        locale = Locale("uz")
                        expanded = false
                    }) {
                        Text(strings.uzbek)
                    }
                    DropdownMenuItem(onClick = {
                        locale = Locale("ru")
                        expanded = false
                    }) {
                        Text(strings.russion)
                    }
                    DropdownMenuItem(onClick = {
                        locale = Locale("en")
                        expanded = false
                    }) {
                        Text(strings.english)
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource("logo.xml"),
                    contentDescription = "Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxWidth(0.414f).fillMaxHeight(0.24f)
                )
                Spacer(modifier = Modifier.width(12.dp).padding(12.dp))
                Text(strings.app_welcome)
                Button(
                    modifier = Modifier.padding(all = 12.dp), onClick = {
                        /*coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                actionLabel = "OK",// Optional: Amal uchun label (masalan, OK)
                                message = "Customized Snackbar", duration = SnackbarDuration.Long
                            )
                        }*/
                        navigator.push(SelectedPlaceScreen(locale.language))
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White, contentColor = AppColors.textColorBlue
                    ), elevation = ButtonDefaults.elevation(0.dp), border = BorderStroke(1.dp, AppColors.textColorBlue)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 24.dp, top = 4.dp, bottom = 4.dp, end = 24.dp
                        ), text = strings.app_next
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Text(
                    text = "${strings.status_network}: ${if (status) strings.active else strings.not_active}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "UUID: 123e4567", fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
            }

            Text(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
                text = "${strings.app_version}: 1.0.0",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(
                    text = "${strings.ip_address}: $ipAddress", fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${strings.mac_address}: $macAddress", fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth(0.3f)
                    .align(Alignment.TopEnd) // Joylashuvni markazga olib kelish
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = Color.White, // Orqa fon rangi
                    contentColor = Color.Green, // Matn rangi
                    actionColor = Color(0xFFFFA726), // Amal uchun tugma rangi
                    shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

}
package com.example.demo.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.demo.screen.auth.LoginScreen
import com.example.demo.util.language.getStrings
import java.util.prefs.Preferences

class SelectedPlaceScreen(val language:String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var locale by remember { mutableStateOf(Locale(language)) }
        val strings = getStrings(locale)
        var firstExpanded by remember { mutableStateOf(false) }
        var secondExpanded by remember { mutableStateOf(false) }
        var selectedFirstOption by remember { mutableStateOf("") }
        var selectedSecondOption by remember { mutableStateOf("") }
        val preference = Preferences.userRoot().node("assesment_foefl_app")
        val token =  preference.get("key_user_token","")
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource("logo.xml"),
                contentDescription = "Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth(0.414f)
                    .fillMaxHeight(0.24f)
            )

            Column {
                Text(
                    text = strings.where_are_you_located,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .zIndex(if (firstExpanded) 1f else 0f)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { firstExpanded = true }
                            .fillMaxWidth(0.4f)
                            .border(1.dp,Color.Gray, RoundedCornerShape(4.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedFirstOption,
                            modifier = Modifier.weight(1f)
                                .padding(10.dp),
                            color = Color.Gray
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    val firstItems = listOf("Povilion 1","Povilion 2","Povilion 3","Povilion 4")
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        expanded = firstExpanded,
                        onDismissRequest = { firstExpanded = false }
                    ) {
                        firstItems.forEach { firstItem->
                            DropdownMenuItem(onClick = {
                                selectedFirstOption = firstItem
                                firstExpanded = false
                                secondExpanded = false
                            }) {
                                Text(firstItem)
                            }
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = strings.select_number_computer,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .zIndex(if (secondExpanded) 1f else 0f) // Ikkinchi menyuga ham z-index berish
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { secondExpanded = true }
                            .fillMaxWidth(0.4f)
                            .border(1.dp,Color.Gray, RoundedCornerShape(4.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedSecondOption,
                            modifier = Modifier.weight(1f).padding(12.dp),
                            color = Color.Gray
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                    val secondItems = listOf("Computer 1","Computer 2","Computer 3","Computer 4")
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        expanded = secondExpanded,
                        onDismissRequest = { secondExpanded = false }
                    ) {
                        secondItems.forEach { secondItem->
                            DropdownMenuItem(onClick = {
                                selectedSecondOption = secondItem
                                secondExpanded = false
                            }) {
                                Text(secondItem)
                            }
                        }

                    }
                }
            }

            Button(onClick = {
                if (token.isNotEmpty()) {
                    navigator.push(SelectExamScreen())
                }else{
                    navigator.push(LoginScreen())
                }

            }, elevation = ButtonDefaults.elevation(0.dp),
                border = BorderStroke(1.dp,if(selectedFirstOption.isNotEmpty() && selectedSecondOption.isNotEmpty()) Color.Blue else Color.Gray),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if(selectedFirstOption.isNotEmpty() && selectedSecondOption.isNotEmpty()) Color.White else Color.LightGray,
                    contentColor = if(selectedFirstOption.isNotEmpty() && selectedSecondOption.isNotEmpty()) Color.Blue else Color.Gray
                )
            ){
                Text("Davom etish")
            }
        }
    }
}
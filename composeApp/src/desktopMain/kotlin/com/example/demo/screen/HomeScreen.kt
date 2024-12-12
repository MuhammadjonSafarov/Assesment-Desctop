package com.example.demo.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlin.random.Random

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf("Select Item") }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /*val bitmap = painterResource(Res.drawable.logo)
                Image(bitmap,"Logo",Modifier.width(200.dp).height(100.dp))*/
                Button(onClick = {
                    navigator.push(DetailScreen())
                }) {
                    Text(text = "Go to Details")
                }
                val items = listOf("Item 1", "Item 2", "Item 3")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(onClick = {
                            selectedItem = item
                            expanded = false
                        }) {
                            Text(item)
                        }
                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Button(onClick = { expanded = !expanded }) {
                        Text(text = selectedItem)
                    }
                    // Dropdown menyu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },  // Foydalanuvchi tashqarisini bosganda yopiladi
                    ) {
                        DropdownMenuItem(onClick = {
                            selectedItem = "Item 1"
                            expanded = false // Menyuni yopish
                        }) {
                            Text("Item 1")
                        }
                        DropdownMenuItem(onClick = {
                            selectedItem = "Item 2"
                            expanded = false
                        }) {
                            Text("Item 2")
                        }
                        DropdownMenuItem(onClick = {
                            selectedItem = "Item 3"
                            expanded = false
                        }) {
                            Text("Item 3")
                        }
                    }
                }
            }

        }
    }
}
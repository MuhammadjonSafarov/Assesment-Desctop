package com.example.demo.screen.listening

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.demo.data.listening.model.SelectQuestionAnswersData

class ListeningFinishScreen(val timerName:String,val answersList:List<SelectQuestionAnswersData>):Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showDialog by remember { mutableStateOf(true) }
        val answers = mutableListOf<SelectQuestionAnswersData>()
        answers.addAll(answersList)

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("Natijalar") },
                text = {
                    Column {
                        Text("Ketkazilgan vaqt: $timerName")
                        LazyColumn {
                            items(answers){ row ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "${row.name} ${row.answer}",
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                            .border(1.dp, Color.Black),
                                        fontSize = 14.sp) }
                            }
                        }
                    }

                },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        navigator.pop()
                    }) {
                        Text("Yopish")
                    }
                }
            )
        }
    }
}
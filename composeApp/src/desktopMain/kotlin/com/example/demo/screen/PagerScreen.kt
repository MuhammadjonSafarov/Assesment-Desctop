package com.example.demo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.launch

class PagerScreen:Screen {

    @Composable
    override fun Content() {
        val lists = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 2", "Item 3", "Item 4", "Item 5", "Item 2", "Item 3", "Item 4", "Item 5", "Item 2", "Item 3", "Item 4", "Item 5", "Item 4", "Item 5", "Item 2", "Item 3", "Item 4", "Item 5", "Item 2", "Item 3", "Item 4", "Item 5")
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        /*LazyRow(
            state = listState,
            modifier = Modifier
        ) {
            itemsIndexed(lists){ index,item->
                Box (
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .fillParentMaxHeight()
                        .padding(16.dp)
                ){
                    Text(
                        modifier = Modifier.align(Alignment.Center)
                            .clickable {
                                if (index < lists.size - 1) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index + 1) // Keyingi elementga o‘tish
                                    }
                                }
                            },
                        text = item,

                    )
                }

            }
        }*/

       /* Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // LazyRow balandligi
            ) {
                items(lists) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    coroutineScope.launch {
                        val prevIndex = (listState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                        listState.animateScrollToItem(prevIndex) // Orqaga o'tish
                    }
                }) {
                    Text("Orqaga")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        val nextIndex = (listState.firstVisibleItemIndex + 1).coerceAtMost(lists.size - 1)
                        listState.animateScrollToItem(nextIndex) // Oldinga o'tish
                    }
                }) {
                    Text("Oldinga")
                }
            }
        }*/
        var offset by remember { mutableStateOf(Offset.Zero) } // Viewning hozirgi pozitsiyasi

        Box(
            modifier = Modifier
                .size(100.dp)
                .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) } // Boxning pozitsiyasini o'rnatish
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume() // Drag eventini iste'mol qilish
                        offset += dragAmount // Offsetni yangilash
                    }
                }
        )
    }
}
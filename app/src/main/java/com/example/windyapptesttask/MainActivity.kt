package com.example.windyapptesttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.windyapptesttask.ui.theme.WindyAppTestTaskTheme
import kotlinx.coroutines.flow.flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WindyAppTestTaskTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var input by remember { mutableStateOf("") }
        var sumResult by remember { mutableStateOf(0) }
        val sumResultList = remember { mutableStateListOf<Int>() }

        val scope = rememberCoroutineScope()

        TextField(
            value = input,
            onValueChange = { input = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = {
                input.toIntOrNull()?.let { n ->
                    val flowsArray = Array(n) { index ->
                        flow {
                            delay((index + 1) * 100L)
                            emit(index + 1)
                        }
                    }

                    scope.launch {
                        flowsArray.forEach { flow ->
                            flow.collect { value ->
                                sumResult += value
                            }
                            delay(100L)
                            sumResultList.add(sumResult)
                        }
                    }
                }
            }
        ) {
            Text("Запустить Flow")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Результаты :")
            Column {
                sumResultList.forEach {
                    Text(
                        text = "$it",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
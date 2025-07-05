package com.example.compose_testing_07_03.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color

@Composable
fun AddTaskScreen(navController: NavController, viewModel: TaskViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var profitability by remember { mutableStateOf(1) }
    var urgency by remember { mutableStateOf(1) }
    var difficulty by remember { mutableStateOf(1) }
    var date by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("添加任务") },
                actions = {
                    TextButton(onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addTask(
                                com.example.compose_testing_07_03.data.model.Task(
                                    id = System.currentTimeMillis(),
                                    title = title,
                                    description = description,
                                    profitability = profitability,
                                    urgency = urgency,
                                    difficulty = difficulty
                                    // isDone 默认 false
                                )
                            )
                            navController.popBackStack()
                        }
                    }) {
                        Text("确定", color = Color.Black)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("标题*") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("内容") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("收益性：")
                Slider(
                    value = profitability.toFloat(),
                    onValueChange = { profitability = it.toInt().coerceIn(1,5) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.weight(1f)
                )
                Text("$profitability")
            }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("紧迫性：")
                Slider(
                    value = urgency.toFloat(),
                    onValueChange = { urgency = it.toInt().coerceIn(1,5) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.weight(1f)
                )
                Text("$urgency")
            }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("难易度：")
                Slider(
                    value = difficulty.toFloat(),
                    onValueChange = { difficulty = it.toInt().coerceIn(1,5) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.weight(1f)
                )
                Text("$difficulty")
            }
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("日期（非必填）") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("如 2024-07-03") }
            )
        }
    }
} 
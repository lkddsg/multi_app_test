package com.example.compose_testing_07_03.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import com.example.compose_testing_07_03.data.model.Task
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskFormScreen(
    navController: NavController,
    viewModel: TaskViewModel = viewModel(),
    taskId: Long? = null
) {
    val editingTask = taskId?.let { id -> viewModel.tasks.find { it.id == id } }
    var title by remember { mutableStateOf(editingTask?.title ?: "") }
    var description by remember { mutableStateOf(editingTask?.description ?: "") }
    var profitability by remember { mutableStateOf(editingTask?.profitability ?: 1) }
    var urgency by remember { mutableStateOf(editingTask?.urgency ?: 1) }
    var difficulty by remember { mutableStateOf(editingTask?.difficulty ?: 1) }
    var date by remember { mutableStateOf(editingTask?.date) }
    var time by remember { mutableStateOf(editingTask?.time) }
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val isEditMode = editingTask != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "编辑任务" else "添加任务") },
                actions = {
                    TextButton(onClick = {
                        if (title.isNotBlank() && date != null) {
                            if (isEditMode) {
                                val updatedTask = editingTask!!.copy(
                                    title = title,
                                    description = description,
                                    profitability = profitability,
                                    urgency = urgency,
                                    difficulty = difficulty,
                                    date = date,
                                    time = time
                                )
                                viewModel.removeTask(editingTask)
                                viewModel.addTask(updatedTask)
                            } else {
                                viewModel.addTask(
                                    Task(
                                        id = System.currentTimeMillis(),
                                        title = title,
                                        description = description,
                                        profitability = profitability,
                                        urgency = urgency,
                                        difficulty = difficulty,
                                        date = date,
                                        time = time
                                    )
                                )
                            }
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
            // 日期选择器
            Button(onClick = { dateDialogState.show() }, modifier = Modifier.fillMaxWidth()) {
                Text(date?.toString() ?: "选择日期（可选）")
            }
            MaterialDialog(dialogState = dateDialogState, buttons = { positiveButton("确定") }) {
                datepicker(
                    initialDate = date ?: LocalDate.now(),
                    title = "选择日期"
                ) { pickedDate ->
                    date = pickedDate
                }
            }
            // 时间选择器
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            Button(onClick = { timeDialogState.show() }, modifier = Modifier.fillMaxWidth()) {
                Text(time?.format(timeFormatter) ?: "选择时间（可选）")
            }
            MaterialDialog(dialogState = timeDialogState, buttons = { positiveButton("确定") }) {
                timepicker(
                    initialTime = time ?: LocalTime.now(),
                    title = "选择时间"
                ) { pickedTime ->
                    time = pickedTime
                }
            }
        }
    }
} 
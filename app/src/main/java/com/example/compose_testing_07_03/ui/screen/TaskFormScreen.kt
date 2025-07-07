package com.example.compose_testing_07_03.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
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
    viewModel: TaskViewModel,
    taskId: Long? = null
) {
    val tasks by viewModel.tasks.collectAsState()
    val editingTask = taskId?.let { id -> tasks.find { it.id == id.toInt() } }
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
                        if (title.isNotBlank()) {
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
                                viewModel.updateTask(updatedTask)
                            } else {
                                viewModel.addTask(
                                    Task(
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { dateDialogState.show() }, 
                    modifier = Modifier.weight(1f)
                ) {
                    Text(date?.toString() ?: "选择日期（可选）")
                }
                if (date != null) {
                    IconButton(onClick = { date = null }) {
                        Text("×", color = Color.Red)
                    }
                }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { timeDialogState.show() }, 
                    modifier = Modifier.weight(1f)
                ) {
                    Text(time?.format(timeFormatter) ?: "选择时间（可选）")
                }
                if (time != null) {
                    IconButton(onClick = { time = null }) {
                        Text("×", color = Color.Red)
                    }
                }
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
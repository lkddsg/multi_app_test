package com.example.compose_testing_07_03.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
import com.example.compose_testing_07_03.data.model.Task
import androidx.navigation.NavController
import java.time.format.DateTimeFormatter

@Composable
fun TaskListScreen(viewModel: TaskViewModel, navController: NavController? = null) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("未完成", "已完成")
    val unfinishedTasksSorted by viewModel.unfinishedTasksSorted.collectAsState()
    val finishedTasksSorted by viewModel.finishedTasksSorted.collectAsState()
    val displayTasks = if (selectedTab == 0) unfinishedTasksSorted else finishedTasksSorted

    Scaffold(
        topBar = {
            if (selectedTab == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { navController?.navigate("task_form") }) {
                        Text("＋", color = Color(0xFF4CAF50), style = MaterialTheme.typography.h4)
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, title ->
                    BottomNavigationItem(
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {})
                }
            }
        }
    ) { innerPadding ->
        if (displayTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "暂无任务", style = MaterialTheme.typography.h6, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayTasks, key = { it.id }) { task ->
                    if (selectedTab == 0) {
                        TaskItem(
                            task = task,
                            onDone = { viewModel.markTaskDone(task) },
                            onRemove = { viewModel.removeTask(task) },
                            doneMode = false,
                            onClick = {
                                navController?.navigate("task_form/${task.id}")
                            }
                        )
                    } else {
                        TaskItem(
                            task = task,
                            onDone = {},
                            onRemove = { viewModel.removeTask(task) },
                            doneMode = true,
                            onClick = {
                                navController?.navigate("task_form/${task.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task, 
    onDone: () -> Unit, 
    onRemove: () -> Unit, 
    doneMode: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        elevation = 4.dp,
        backgroundColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (!doneMode && task.date != null) Color(0x88FFA959) // 半透明ffa959
                else if (doneMode) Color(0x8832CD32)
                else Color.Transparent
            )
            .clickable { onClick() }
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = task.title, style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        if (!doneMode) {
                            IconButton(onClick = onDone) {
                                Text("√", color = Color(0xFF4CAF50))
                            }
                        }
                        IconButton(onClick = onRemove) {
                            Text("×", color = Color.Red)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = task.description, style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier.height(8.dp))
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "优先级：${String.format("%.2f", task.priority)}",
                        style = MaterialTheme.typography.caption
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (task.date != null) {
                        Text(
                            text = task.date.format(dateFormatter),
                            style = MaterialTheme.typography.caption
                        )
                    }
                    if (task.time != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = task.time.format(timeFormatter),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
} 
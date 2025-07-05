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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
import com.example.compose_testing_07_03.data.model.Task
import androidx.navigation.NavController

@Composable
fun TaskListScreen(viewModel: TaskViewModel = viewModel(), navController: NavController? = null) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("未完成", "已完成")
    val tasks = if (selectedTab == 0) viewModel.unfinishedTasks else viewModel.finishedTasks

    Scaffold(
        topBar = {
            if (selectedTab == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { navController?.navigate("add_task") }) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                if (selectedTab == 0) {
                    TaskItem(
                        task = task,
                        onDone = { viewModel.markTaskDone(task) },
                        onRemove = { viewModel.removeTask(task) },
                        doneMode = false,
                        onClick = { 
                            // 点击任务项后跳转到功能选择页面
                            navController?.navigate("add_task")
                        }
                    )
                } else {
                    TaskItem(
                        task = task,
                        onDone = {},
                        onRemove = { viewModel.removeTask(task) },
                        doneMode = true,
                        onClick = { 
                            // 点击已完成任务项后也跳转到功能选择页面
                            navController?.navigate("add_task")
                        }
                    )
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
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (!doneMode) Color(0x8832CD32) else Color.Transparent
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
                Text(text = "优先级：${String.format("%.2f", task.priority)}", style = MaterialTheme.typography.caption)
            }
        }
    }
} 
package com.example.compose_testing_07_03.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
import com.example.compose_testing_07_03.data.model.Task

@Composable
fun TaskListScreen(viewModel: TaskViewModel = viewModel()) {
    val tasks = viewModel.tasks

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onRemove = { viewModel.removeTask(task) }
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, onRemove: () -> Unit) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = task.title, style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        IconButton(onClick = onRemove) {
                            Text("√")
                        }
                        IconButton(onClick = onRemove) {
                            Text("×")
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
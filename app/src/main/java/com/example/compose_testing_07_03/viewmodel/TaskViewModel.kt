package com.example.compose_testing_07_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.compose_testing_07_03.data.model.Task
import com.example.compose_testing_07_03.data.repository.TaskRepository

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    init {
        // 初始化时加载本地模拟数据
        _tasks.clear()
        _tasks.addAll(repository.getInitialTasks())
    }

    fun addTask(task: Task) {
        repository.addTask(task)
        refreshTasks()
    }

    fun removeTask(task: Task) {
        repository.removeTask(task)
        refreshTasks()
    }

    private fun refreshTasks() {
        _tasks.clear()
        _tasks.addAll(repository.getTasks())
    }

    fun clearTasks() {
        repository.clearTasks()
        refreshTasks()
    }

    // 按综合优先级排序
    private fun sortTasks() {
        _tasks.sortByDescending { it.priority }
    }

    // 其他管理方法（删除、编辑等）可后续补充
} 
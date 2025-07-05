package com.example.compose_testing_07_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.compose_testing_07_03.data.model.Task
import com.example.compose_testing_07_03.data.repository.TaskRepository
import android.util.Log

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks
    val unfinishedTasks: List<Task> get() = _tasks.filter { !it.isDone }
    val finishedTasks: List<Task> get() = _tasks.filter { it.isDone }

    // 新增：分组排序后的未完成和已完成任务
    val unfinishedTasksSorted: List<Task>
        get() {
            val (dated, undated) = unfinishedTasks.partition { it.date != null }
            return dated.sortedByDescending { it.priority } + undated.sortedByDescending { it.priority }
        }
    val finishedTasksSorted: List<Task>
        get() {
            val (dated, undated) = finishedTasks.partition { it.date != null }
            return dated.sortedByDescending { it.priority } + undated.sortedByDescending { it.priority }
        }

    init {
        _tasks.clear()
        _tasks.addAll(repository.getInitialTasks())
        sortTasks()
    }

    fun addTask(task: Task) {
        repository.addTask(task)
        _tasks.add(task)
        sortTasks()
    }

    fun removeTask(task: Task) {
        repository.removeTask(task)
        _tasks.removeIf { it.id == task.id }
        sortTasks()
    }

    fun clearTasks() {
        repository.clearTasks()
        _tasks.clear()
    }

    private fun sortTasks() {
        _tasks.sortByDescending { it.priority }
        // 打印所有任务内容
        Log.d("TaskViewModel", "当前_tasks内容：")
        _tasks.forEach { task ->
            Log.d("TaskViewModel", task.toString())
        }
    }

    fun markTaskDone(task: Task) {
        val updatedTask = task.copy(isDone = true)
        repository.updateTask(updatedTask)
        val index = _tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            _tasks[index] = updatedTask
            sortTasks()
        }
    }
}
package com.example.compose_testing_07_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_testing_07_03.data.model.Task
import com.example.compose_testing_07_03.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalTime

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    // 用 StateFlow 持有任务列表
    private val _tasks = repository.getTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // 只读的未完成/已完成/排序后的任务
    val unfinishedTasks: StateFlow<List<Task>> = tasks.map { it.filter { t -> !t.isDone } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val finishedTasks: StateFlow<List<Task>> = tasks.map { it.filter { t -> t.isDone } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val unfinishedTasksSorted: StateFlow<List<Task>> = unfinishedTasks.map { list ->
        val (dated, undated) = list.partition { it.date != null }
        val datedSorted = dated.sortedWith(compareBy<Task> { it.date }
            .thenBy { it.time ?: LocalTime.MAX }
            .thenByDescending { it.priority })
        val undatedSorted = undated.sortedByDescending { it.priority }
        datedSorted + undatedSorted
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val finishedTasksSorted: StateFlow<List<Task>> = finishedTasks.map { list ->
        val (dated, undated) = list.partition { it.date != null }
        val datedSorted = dated.sortedWith(compareBy<Task> { it.date }
            .thenBy { it.time ?: LocalTime.MAX }
            .thenByDescending { it.priority })
        val undatedSorted = undated.sortedByDescending { it.priority }
        datedSorted + undatedSorted
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 新增/删除/更新/清空
    fun addTask(task: Task) = viewModelScope.launch { repository.addTask(task) }
    fun removeTask(task: Task) = viewModelScope.launch { repository.removeTask(task) }
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task) }
    fun clearTasks() = viewModelScope.launch { repository.clearTasks() }
    fun markTaskDone(task: Task) = updateTask(task.copy(isDone = true))
}
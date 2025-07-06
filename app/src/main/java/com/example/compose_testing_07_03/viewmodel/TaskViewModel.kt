package com.example.compose_testing_07_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.compose_testing_07_03.data.model.Task
import com.example.compose_testing_07_03.data.repository.TaskRepository
import android.util.Log
import java.time.LocalTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class TaskViewModel : ViewModel() {
    // 使用单例的TaskRepository
    private val repository = TaskRepository
    private var _isInitialized = false
    
    // 使用委托属性，自动处理初始化
    private val _tasks by lazy {
        _isInitialized = true
        Log.d("TaskViewModel", "任务列表已懒加载初始化")
        mutableStateListOf<Task>().apply {
            // 不再同步初始化数据，改为异步加载
            Log.d("TaskViewModel", "初始为空，等待异步加载")
        }
    }
    
    val tasks: List<Task> get() = _tasks
    val unfinishedTasks: List<Task> get() = tasks.filter { !it.isDone }
    val finishedTasks: List<Task> get() = tasks.filter { it.isDone }

    // 懒加载：分组排序后的未完成和已完成任务
    val unfinishedTasksSorted: List<Task>
        get() {
            val (dated, undated) = unfinishedTasks.partition { it.date != null }
            // 有日期的任务：先按日期时间排序（越早越前面），再按优先级排序
            val datedSorted = dated.sortedWith(compareBy<Task> { it.date }
                .thenBy { it.time ?: LocalTime.MAX }
                .thenByDescending { it.priority })
            // 无日期的任务：按优先级排序
            val undatedSorted = undated.sortedByDescending { it.priority }
            return datedSorted + undatedSorted
        }
    val finishedTasksSorted: List<Task>
        get() {
            val (dated, undated) = finishedTasks.partition { it.date != null }
            // 有日期的任务：先按日期时间排序（越早越前面），再按优先级排序
            val datedSorted = dated.sortedWith(compareBy<Task> { it.date }
                .thenBy { it.time ?: LocalTime.MAX }
                .thenByDescending { it.priority })
            // 无日期的任务：按优先级排序
            val undatedSorted = undated.sortedByDescending { it.priority }
            return datedSorted + undatedSorted
        }

    // 新增：异步加载方法
    fun loadTasksAsync() {
        viewModelScope.launch {
            _tasks.clear()
            //delay(1000) // 模拟异步加载
            _tasks.addAll(repository.getInitialTasks())
            Log.d("TaskViewModel", "异步加载完成，任务数：${_tasks.size}")
        }
    }

    fun addTask(task: Task) {
        repository.addTask(task)
        _tasks.add(task)
        Log.d("TaskViewModel", "添加任务: ${task.title}, 当前任务总数: ${_tasks.size}")
    }

    fun removeTask(task: Task) {
        repository.removeTask(task)
        _tasks.removeIf { it.id == task.id }
        Log.d("TaskViewModel", "删除任务: ${task.title}, 当前任务总数: ${_tasks.size}")
    }

    fun clearTasks() {
        repository.clearTasks()
        _tasks.clear()
        _isInitialized = false
        Log.d("TaskViewModel", "清空所有任务")
    }

    fun updateTask(updatedTask: Task) {
        repository.updateTask(updatedTask)
        val index = _tasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            _tasks[index] = updatedTask
            Log.d("TaskViewModel", "更新任务: ${updatedTask.title}")
        }
    }

    fun markTaskDone(task: Task) {
        val updatedTask = task.copy(isDone = true)
        updateTask(updatedTask)
        Log.d("TaskViewModel", "标记任务完成: ${task.title}")
    }
    
    // 手动触发排序（可选，用于调试）
    fun forceSort() {
        _tasks.sortWith(compareBy<Task> { it.date == null }  // 无日期的排在后面
            .thenBy { it.date }  // 有日期的按日期排序
            .thenBy { it.time ?: LocalTime.MAX }  // 按时间排序
            .thenByDescending { it.priority })  // 最后按优先级排序
        
        Log.d("TaskViewModel", "手动排序完成，当前_tasks内容：")
        _tasks.forEach { task ->
            Log.d("TaskViewModel", task.toString())
        }
    }
}
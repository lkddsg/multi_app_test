package com.example.compose_testing_07_03.data.repository

import com.example.compose_testing_07_03.data.model.Task

class TaskRepository {
    // 这里用内存列表模拟数据源，后续可替换为数据库或网络
    private val taskList = mutableListOf<Task>()

    init {
        // 初始化时添加初始任务
        taskList.addAll(getInitialTasks())
    }

    fun getTasks(): List<Task> = taskList.sortedByDescending { it.priority }

    fun addTask(task: Task) {
        taskList.add(task)
    }

    fun removeTask(task: Task) {
        taskList.removeAll { it.id == task.id }
    }

    fun updateTask(updatedTask: Task) {
        val index = taskList.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            taskList[index] = updatedTask
        }
    }

    fun clearTasks() {
        taskList.clear()
    }

    // 新增：返回本地模拟任务列表
    fun getInitialTasks(): List<Task> {
        return listOf(
            Task(
                id = 1L,
                title = "学习 Compose",
                description = "掌握 Jetpack Compose 基础用法",
                profitability = 4,
                urgency = 3,
                difficulty = 2
            ),
            Task(
                id = 2L,
                title = "完成项目作业",
                description = "按时提交 Android 项目",
                profitability = 5,
                urgency = 4,
                difficulty = 3
            ),
            Task(
                id = 3L,
                title = "阅读文档",
                description = "阅读 Compose 官方文档",
                profitability = 3,
                urgency = 2,
                difficulty = 2
            )
        )
    }
} 
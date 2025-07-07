package com.example.compose_testing_07_03.data.repository

import com.example.compose_testing_07_03.data.model.Task
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 任务仓库单例类
 * 确保整个应用中使用同一个数据源
 */
class TaskRepository(private val taskDao: TaskDao) {
    // 这里用内存列表模拟数据源，后续可替换为数据库或网络
    private val taskList = mutableListOf<Task>()

    init {
        // 初始化时添加初始任务
//        taskList.addAll(getInitialTasks())
    }

    fun getTasks(): Flow<List<Task>> = taskDao.getTasks()

    suspend fun addTask(task: Task) = taskDao.addTask(task)

    suspend fun removeTask(task: Task) = taskDao.removeTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun clearTasks() = taskDao.clearTasks()

    // 新增：返回本地模拟任务列表
//    fun getInitialTasks(): List<Task> {
//        return listOf(
//            Task(
//                id = 1L,
//                title = "学习 Compose",
//                description = "掌握 Jetpack Compose 基础用法",
//                profitability = 4,
//                urgency = 3,
//                difficulty = 2,
//                date = LocalDate.parse("2025-07-10")
//            ),
//            Task(
//                id = 2L,
//                title = "完成项目作业",
//                description = "按时提交 Android 项目",
//                profitability = 5,
//                urgency = 4,
//                difficulty = 3
//            ),
//            Task(
//                id = 3L,
//                title = "阅读文档",
//                description = "阅读 Compose 官方文档",
//                profitability = 3,
//                urgency = 2,
//                difficulty = 2
//            ),
//        )
//    }
} 
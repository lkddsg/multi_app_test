package com.example.compose_testing_07_03.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

// 事项数据类，包含收益性、紧迫性、难易度三个维度
@Entity(tableName = "task")
data class Task(
//    val id: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val urgency: Int,       // 紧迫性 1~5
    val profitability: Int, // 收益性 1~5
    val difficulty: Int,    // 难易度 1~5
    val date: LocalDate? = null,    // 选填，日期（如2024-07-07）
    val time: LocalTime? = null,    // 选填，时间（如08:59），不需要时为null
    val isDone: Boolean = false // 新增：是否已完成
) {
    // 综合优先级，数值越高优先级越高（可自定义权重）
    val priority: Double
        get() = urgency * 0.6 + profitability * 0.4 - difficulty * 0.3
} 
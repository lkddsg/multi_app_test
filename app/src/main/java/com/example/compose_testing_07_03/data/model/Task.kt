package com.example.compose_testing_07_03.data.model

// 事项数据类，包含收益性、紧迫性、难易度三个维度

data class Task(
    val id: Long,
    val title: String,
    val description: String = "",
    val profitability: Int, // 收益性 1~5
    val urgency: Int,       // 紧迫性 1~5
    val difficulty: Int     // 难易度 1~5
) {
    // 综合优先级，数值越高优先级越高（可自定义权重）
    val priority: Double
        get() = profitability * 0.5 + urgency * 0.4 - difficulty * 0.3
} 
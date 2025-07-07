package com.example.compose_testing_07_03.data.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.compose_testing_07_03.data.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER BY (urgency * 0.6 + profitability * 0.4 - difficulty * 0.3) DESC")
    fun getTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task)

    @Delete
    suspend fun removeTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM task")
    suspend fun clearTasks()
}
package com.example.compose_testing_07_03.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.compose_testing_07_03.data.model.Task
import com.example.compose_testing_07_03.data.model.Converters
import com.example.compose_testing_07_03.data.repository.TaskDao

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

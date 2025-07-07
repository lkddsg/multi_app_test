package com.example.compose_testing_07_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import android.util.Log
import com.example.compose_testing_07_03.data.repository.TaskRepository

/**
 * TaskViewModel的单例管理器
 * 确保整个应用中使用同一个TaskViewModel实例
 */
object TaskViewModelManager {
    
    // 全局ViewModelStore，用于管理ViewModel生命周期
    private val globalViewModelStore = ViewModelStore()
    
    // 全局ViewModelStoreOwner，修正为getViewModelStore()方法
    private val globalViewModelStoreOwner = object : ViewModelStoreOwner {
        override fun getViewModelStore(): ViewModelStore = globalViewModelStore
    }
    
    // 新增：保存全局 repository
    private var repository: TaskRepository? = null
    
    // 新增：初始化方法，必须先调用
    fun init(repository: TaskRepository) {
        this.repository = repository
    }
    
    // 工厂方法
    private val globalViewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return TaskViewModel(repository!!) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    
    // 获取全局TaskViewModel实例
    fun getTaskViewModel(): TaskViewModel {
        if (repository == null) throw IllegalStateException("TaskViewModelManager未初始化，请先调用init()")
        return ViewModelProvider(
            globalViewModelStoreOwner,
            globalViewModelFactory
        )[TaskViewModel::class.java]
    }
    
    // 清理资源（在应用退出时调用）
    fun clear() {
        Log.d("TaskViewModelManager", "清理全局ViewModel资源")
        globalViewModelStore.clear()
    }
} 
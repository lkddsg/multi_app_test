package com.example.compose_testing_07_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import android.util.Log

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
    
    // 全局ViewModelFactory
    private val globalViewModelFactory = viewModelFactory {
        initializer {
            Log.d("TaskViewModelManager", "创建新的TaskViewModel实例")
            TaskViewModel()
        }
    }
    
    // 获取全局TaskViewModel实例
    fun getTaskViewModel(): TaskViewModel {
        Log.d("TaskViewModelManager", "获取TaskViewModel实例")
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
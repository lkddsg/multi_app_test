package com.example.compose_testing_07_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.compose_testing_07_03.ui.theme.Compose_testing_07_03Theme
import androidx.navigation.compose.rememberNavController
import com.example.compose_testing_07_03.ui.screen.FeatureSelectScreen
import com.example.compose_testing_07_03.ui.screen.TaskListScreen
import com.example.compose_testing_07_03.ui.screen.TaskFormScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Modifier
import com.example.compose_testing_07_03.viewmodel.TaskViewModelManager
import androidx.room.Room
import com.example.compose_testing_07_03.data.local.AppDatabase
import com.example.compose_testing_07_03.data.repository.TaskRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化数据库和仓库
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()
        val repository = TaskRepository(db.taskDao())
        // 初始化全局 ViewModel 管理器
        TaskViewModelManager.init(repository)

        setContent {
            Compose_testing_07_03Theme {
                MainScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清理全局ViewModel资源
        TaskViewModelManager.clear()
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // 用MutableStateMap记录每个功能的加载状态
    val loadedMap = remember { mutableStateMapOf<String, Boolean>() }

    NavHost(
        navController = navController,
        startDestination = "feature_select"
    ) {
        // 功能选择页面 - 始终加载
        composable("feature_select") {
            FeatureSelectScreen(onPriorityClick = {
                navController.navigate("priority")
            })
        }

        // 任务列表页面 - 懒加载ViewModel和组件
        composable("priority") {
            LazyLoadScreen(
                featureKey = "priority",
                loadedMap = loadedMap
            ) {
                val taskViewModel = TaskViewModelManager.getTaskViewModel()
                TaskListScreen(viewModel = taskViewModel, navController = navController)
            }
        }

        // 任务表单页面 - 懒加载ViewModel和组件
        composable("task_form") {
            LazyLoadScreen(
                featureKey = "task_form",
                loadedMap = loadedMap
            ) {
                val taskViewModel = TaskViewModelManager.getTaskViewModel()
                TaskFormScreen(navController = navController, viewModel = taskViewModel)
            }
        }

        // 任务编辑页面 - 懒加载ViewModel和组件
        composable(
            route = "task_form/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId")
            LazyLoadScreen(
                featureKey = "task_form_edit_${taskId}",
                loadedMap = loadedMap
            ) {
                val taskViewModel = TaskViewModelManager.getTaskViewModel()
                TaskFormScreen(navController = navController, viewModel = taskViewModel, taskId = taskId)
            }
        }
    }
}

@Composable
fun LazyLoadScreen(
    featureKey: String,
    loadedMap: MutableMap<String, Boolean>,
    content: @Composable () -> Unit
) {
    // 只要该功能没加载过就显示加载动画
    var isLoaded by remember { mutableStateOf(loadedMap[featureKey] == true) }

    LaunchedEffect(featureKey) {
        if (!isLoaded) {
            isLoaded = true
            loadedMap[featureKey] = true
        }
    }

    if (isLoaded) {
        content()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    }
}

// 保留 NavItem 数据类（如有需要）
data class NavItem(val title: String, val route: String)

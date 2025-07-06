package com.example.compose_testing_07_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.compose_testing_07_03.ui.screen.BlankScreen
import com.example.compose_testing_07_03.ui.theme.Compose_testing_07_03Theme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.Scaffold
import com.example.compose_testing_07_03.ui.screen.TaskListScreen
import com.example.compose_testing_07_03.ui.screen.FeatureSelectScreen
import com.example.compose_testing_07_03.ui.screen.TaskFormScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_testing_07_03.viewmodel.TaskViewModel
import com.example.compose_testing_07_03.viewmodel.TaskViewModelManager
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.navigation.NavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                // 页面首次进入时异步加载
                LaunchedEffect(Unit) {
                    taskViewModel.loadTasksAsync()
                }
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

// 更高级的懒加载方案（可选）
// 这种方式可以实现真正的按需加载，但会增加代码复杂度
/*
@Composable
fun AdvancedMainScreen() {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("feature_select") }
    
    NavHost(
        navController = navController,
        startDestination = "feature_select"
    ) {
        composable("feature_select") {
            FeatureSelectScreen(onPriorityClick = {
                currentRoute = "priority"
                navController.navigate("priority")
            })
        }
        
        // 只有在需要时才添加路由
        if (currentRoute == "priority") {
            composable("priority") {
                val taskViewModel = TaskViewModelManager.getTaskViewModel()
                TaskListScreen(viewModel = taskViewModel, navController = navController)
            }
        }
        
        if (currentRoute in listOf("task_form", "task_form_edit")) {
            composable("task_form") {
                val taskViewModel = TaskViewModelManager.getTaskViewModel()
                TaskFormScreen(navController = navController, viewModel = taskViewModel)
            }
            
            composable(
                route = "task_form/{taskId}",
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getLong("taskId")
                val taskViewModel = TaskViewModelManager.getTaskViewModel()
                TaskFormScreen(navController = navController, viewModel = taskViewModel, taskId = taskId)
            }
        }
    }
}
*/

data class NavItem(val title: String, val route: String)
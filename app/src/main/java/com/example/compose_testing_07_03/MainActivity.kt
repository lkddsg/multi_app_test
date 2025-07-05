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
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose_testing_07_03Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "feature_select"
    ) {
        composable("feature_select") {
            FeatureSelectScreen(onPriorityClick = {
                navController.navigate("priority")
            })
        }
        composable("priority") {
            TaskListScreen(viewModel = taskViewModel, navController = navController)
        }
        composable("task_form") {
            TaskFormScreen(navController = navController, viewModel = taskViewModel)
        }
        composable(
            route = "task_form/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId")
            TaskFormScreen(navController = navController, viewModel = taskViewModel, taskId = taskId)
        }
    }
}

data class NavItem(val title: String, val route: String)
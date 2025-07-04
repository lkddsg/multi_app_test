package com.example.compose_testing_07_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.compose_testing_07_03.ui.screen.PriorityListScreen
import com.example.compose_testing_07_03.ui.screen.BlankScreen
import com.example.compose_testing_07_03.ui.theme.Compose_testing_07_03Theme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.Scaffold
import com.example.compose_testing_07_03.ui.screen.TaskListScreen

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
    val items = listOf(
        NavItem("优先级", "priority"),
        NavItem("空白1", "blank1"),
        NavItem("空白2", "blank2"),
        NavItem("空白3", "blank3")
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    BottomNavigationItem(
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { /* 可选：可放置Icon()，此处留空 */ }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "priority",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("priority") { TaskListScreen() }
            composable("blank1") { BlankScreen() }
            composable("blank2") { BlankScreen() }
            composable("blank3") { BlankScreen() }
        }
    }
}

data class NavItem(val title: String, val route: String)
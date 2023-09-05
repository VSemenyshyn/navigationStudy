@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navigationstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigationstudy.ui.theme.NavigationStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationStudyTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation(
                            items = listOf(
                                BottomNavItem(
                                    name = "Home",
                                    route = "home",
                                    icon = Icons.Default.Home
                                ),
                                BottomNavItem(
                                    name = "Chat",
                                    route = "chat",
                                    icon = Icons.Default.Notifications,
                                    badgeCount = 18
                                ),
                                BottomNavItem(
                                    name = "Setting",
                                    route = "setting",
                                    icon = Icons.Default.Settings,
                                    badgeCount = 3123
                                )
                            ),
                            navController = navController,
                            onItemClick = { navController.navigate(it.route) })
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        contentAlignment = Alignment.Center
                    ) {
                        Navigation(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen()
        }
        composable(route = "chat") {
            Chat()
        }
        composable(route = "setting") {
            Settings()
        }
    }
}

@Composable
fun BottomNavigation(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomAppBar(
        modifier = modifier,
        containerColor = BottomAppBarDefaults.containerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomAppBarDefaults.ContainerElevation,
    ) {
        items.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(it) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (it.badgeCount > 0)
                            BadgedBox(badge = {
                                Box(modifier = Modifier.background(color = Color.Red)) {
                                    Text(text = it.badgeCount.toString(), color = Color.White)
                                }
                            }
                            ) {
                                Icon(imageVector = it.icon, contentDescription = it.name)
                            }
                        else
                            Icon(imageVector = it.icon, contentDescription = it.name)

                        if (selected)
                            Text(text = it.name, textAlign = TextAlign.Center, fontSize = 10.sp)
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home screen")
    }
}

@Composable
fun Chat() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Chat screen")
    }
}

@Composable
fun Settings() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings screen")
    }
}


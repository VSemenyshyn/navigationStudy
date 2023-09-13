@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navigationstudy.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.navigationstudy.main.home.EditProfile
import com.example.navigationstudy.main.home.Profile
import com.example.navigationstudy.ui.theme.NavigationStudyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationStudyTheme {
                val navController = rememberNavController()
//                BackHandler {
//                    navController.printBackStack()
//                }
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
                            onItemClick = {
                                navController.navigate(it.route) {
                                    popUpTo("profile/-1") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        )
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

@OptIn(ExperimentalAnimationApi::class, InternalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController) {

    val noEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(
            animationSpec = tween(durationMillis = 0),
            initialAlpha = 0f
        )
    }

    val noExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(
            animationSpec = tween(durationMillis = 0),
            targetAlpha = 0f
        )
    }

    NavHost(navController = navController, startDestination = "home") {
        navigation(startDestination = "profile/{id}", route = "home") {
            composable(
                route = "profile/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                println(
                    "NavigationStudy debug - profile - transaction currentState/isRunning/targetState/lifecycle/id: " +
                            "${this.transition.currentState}/${this.transition.isRunning}/${this.transition.targetState}/${it.lifecycle.currentState}/${it.arguments?.getInt("id")}"
                )

                if (!transition.isRunning && transition.currentState == EnterExitState.Visible && it.lifecycle.currentState == Lifecycle.State.RESUMED) {
                    println("NavigationStudy debug - profile: id = ${it.arguments?.getInt("id")}")
                }

                Profile(
                    onEditProfileClick = {
                        navController.navigate("edit_profile")
                    }
                )
            }
            composable(route = "edit_profile") {
                EditProfile(
                    onBackToProfileClick = {
                        navController.navigate("profile/777") {
                            popUpTo("profile") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        navigation(startDestination = "all_messages", route = "chat") {
            composable(route = "all_messages") {
                Chat()
            }
        }
        navigation(startDestination = "setting_route", route = "setting") {
            composable(route = "setting_route") {
                Settings()
            }
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
            val selected = it.route == backStackEntry.value?.destination?.parent?.route
            NavigationBarItem(
                selected = selected,
                onClick = { if (!selected) onItemClick(it) },
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

fun NavController.printBackStack() {
    println("--------------------")
    currentBackStack.value.forEach {
        println("screen : ${it.destination.route}")
    }
    println("--------------------")
}


package xyz.sattar.javid.marketmessage.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import xyz.sattar.javid.marketmessage.ui.home.HomeScreen
import xyz.sattar.javid.marketmessage.ui.message_creation.messageCreationGraph
import xyz.sattar.javid.marketmessage.ui.messages.MessagesScreen
import xyz.sattar.javid.marketmessage.ui.settings.SettingsScreen


@Composable
fun MarketMessageApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Check if the current destination is one of the bottom bar items
    val showBottomBar = AppDestinations.entries.any { appDest ->
        currentDestination?.hierarchy?.any { it.route == appDest.screen::class.qualifiedName } == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                NavigationBar {
                    AppDestinations.entries.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.screen::class.qualifiedName } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    destination.icon,
                                    contentDescription = destination.label
                                )
                            },
                            label = { Text(destination.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.screen) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onCreateMessageClick = {
                        navController.navigate(Screen.MessageCreationGraph)
                    }
                )
            }
            composable<Screen.Messages> { MessagesScreen() }
            composable<Screen.Settings> { SettingsScreen() }

            // Nested Navigation Graph for Message Creation
            messageCreationGraph(navController)
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
) {
    Home("خانه", Icons.Default.Home, Screen.Home),
    Messages("پیام‌ها", Icons.Default.Email, Screen.Messages),
    Settings("تنظیمات", Icons.Default.Settings, Screen.Settings)
}

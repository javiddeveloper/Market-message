package xyz.sattar.javid.marketmessage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import xyz.sattar.javid.marketmessage.ui.home.HomeScreen
import xyz.sattar.javid.marketmessage.ui.messages.MessagesScreen
import xyz.sattar.javid.marketmessage.ui.navigation.Screen
import xyz.sattar.javid.marketmessage.ui.settings.SettingsScreen
import xyz.sattar.javid.marketmessage.ui.theme.MarketMessageTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketMessageTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MarketMessageApp()
                }
            }
        }
    }
}

@Composable
fun MarketMessageApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentDestination?.hierarchy?.any { it.route == destination.screen::class.qualifiedName } == true,
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
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Screen.Home> { HomeScreen() }
                composable<Screen.Messages> { MessagesScreen() }
                composable<Screen.Settings> { SettingsScreen() }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val screen: Screen,
) {
    HOME("خانه", Icons.Default.Home, Screen.Home),
    MESSAGES("مدیریت پیام‌ها", Icons.Default.Email, Screen.Messages),
    SETTINGS("تنظیمات", Icons.Default.Settings, Screen.Settings),
}

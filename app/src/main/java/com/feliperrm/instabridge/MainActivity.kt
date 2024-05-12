package com.feliperrm.instabridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.feliperrm.instabridge.data.countries.CountriesListScreen
import com.feliperrm.instabridge.data.home.DataScreen
import com.feliperrm.instabridge.data.success.SuccessScreen
import com.feliperrm.instabridge.ui.theme.InstaBridgePurchaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreenWithBottomBar()
        }
    }
}

private val bottomBarScreens = listOf(
    Screen.Data,
    Screen.Wifi,
    Screen.Menu
)

@Composable
fun MainScreenWithBottomBar() {
    val navController = rememberNavController()
    InstaBridgePurchaseTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavigation(navController) }
        ) { innerPadding ->
            NavHost(navController, startDestination = Screen.Data.route, Modifier.padding(innerPadding)) {
                composable(Screen.Data.route) { DataScreen(navController = navController) }
                composable(Screen.CountriesList.route) { CountriesListScreen(navController = navController) }
                composable(Screen.Success.route) { SuccessScreen(navController = navController) }
                composable(Screen.Wifi.route) { Text(text = "Wifi screen will be here") }
                composable(Screen.Menu.route) { Text(text = "Menu screen will be here") }
            }
        }
    }
}

@Composable
private fun BottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = Color.Black
    ) {
        val currentDestination = navBackStackEntry?.destination
        bottomBarScreens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        val color by animateColorAsState(
                            targetValue = if (isSelected) Color.White else Color.Gray,
                            animationSpec = spring(stiffness = Spring.StiffnessLow)
                        )
                        Icon(screen.iconVector ?: Icons.Filled.Diamond, tint = color, contentDescription = null)
                        Text(screen.resourceId?.let { stringResource(screen.resourceId) } ?: "", color = color)
                    }
                },
                label = {
                    // Label is added together with the icon because we want the selection to include it
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstaBridgePurchaseTheme {
        MainScreenWithBottomBar()
    }
}
package com.vitalcare.serenia.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vitalcare.serenia.features.home.presentation.navigation.HomeNavGraphRoute
import com.vitalcare.serenia.features.home.presentation.navigation.HomeRoute
import com.vitalcare.serenia.features.home.presentation.navigation.homeNavGraph

@Composable
fun AppNavHost(navController: NavHostController) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val isHomeVisible = backStackEntry?.destination?.hasRoute<HomeRoute>() ?: true

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (isHomeVisible) {
                BottomNavigationBar(
                    selectedItem = BottomNavItem.HOME,
                    onItemClick = { item ->
                        when (item) {
                            BottomNavItem.HOME -> navController.navigate(HomeNavGraphRoute) {
                                launchSingleTop = true
                            }
                            // The remaining tabs will navigate once their screens exist
                            else -> Unit
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeNavGraphRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            homeNavGraph(navController)
        }
    }
}

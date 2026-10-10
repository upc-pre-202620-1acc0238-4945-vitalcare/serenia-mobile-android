package com.vitalcare.serenia.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vitalcare.serenia.features.alertsandsafety.presentation.navigation.alertsAndSafetyNavGraph
import com.vitalcare.serenia.features.checkin.presentation.navigation.checkInNavGraph
import com.vitalcare.serenia.features.home.presentation.navigation.HomeNavGraphRoute
import com.vitalcare.serenia.features.home.presentation.navigation.HomeRoute
import com.vitalcare.serenia.features.home.presentation.navigation.homeNavGraph
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.CircleRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.FamilyNavGraphRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.FamilyRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.RemindersRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.SocialCompanionshipNavGraphRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.familyNavGraph
import com.vitalcare.serenia.features.socialcompanionship.presentation.navigation.socialCompanionshipNavGraph

@Composable
fun AppNavHost(navController: NavHostController) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    // The bar is only shown on the main screen of each tab
    val selectedItem = when {
        destination == null || destination.hasRoute<HomeRoute>() -> BottomNavItem.HOME
        destination.hasRoute<RemindersRoute>() -> BottomNavItem.REMINDERS
        destination.hasRoute<FamilyRoute>() || destination.hasRoute<CircleRoute>() -> BottomNavItem.FAMILY
        else -> null
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (selectedItem != null) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemClick = { item ->
                        val route: Any? = when (item) {
                            BottomNavItem.HOME -> HomeNavGraphRoute
                            BottomNavItem.REMINDERS -> SocialCompanionshipNavGraphRoute
                            BottomNavItem.FAMILY -> FamilyNavGraphRoute
                            // The remaining tabs will navigate once their screens exist
                            else -> null
                        }

                        if (route != null) {
                            navController.navigate(route) {
                                // Keeps a single copy of each tab and its state when switching
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
            checkInNavGraph(navController)
            alertsAndSafetyNavGraph(navController)
            socialCompanionshipNavGraph(navController)
            familyNavGraph(navController)
        }
    }
}

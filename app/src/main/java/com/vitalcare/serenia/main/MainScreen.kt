package com.vitalcare.serenia.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vitalcare.serenia.features.alertsandsafety.presentation.alertsAndSafetyNavGraph
import com.vitalcare.serenia.features.checkin.presentation.checkInNavGraph
import com.vitalcare.serenia.features.home.presentation.HomeNavGraphRoute
import com.vitalcare.serenia.features.home.presentation.HomeRoute
import com.vitalcare.serenia.features.home.presentation.homeNavGraph
import com.vitalcare.serenia.features.iam.presentation.IamNavGraphRoute
import com.vitalcare.serenia.features.iam.presentation.iamNavGraph
import com.vitalcare.serenia.features.socialcompanionship.presentation.CircleRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.FamilyNavGraphRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.FamilyRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.RemindersRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.familyNavGraph
import com.vitalcare.serenia.features.socialcompanionship.presentation.socialCompanionshipNavGraph

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    // The bar is only shown on the main screen of each tab
    val selectedItem = when {
        destination?.hasRoute<HomeRoute>() == true -> NavigationItem.HOME
        destination?.hasRoute<RemindersRoute>() == true -> NavigationItem.REMINDERS
        destination?.hasRoute<FamilyRoute>() == true || destination?.hasRoute<CircleRoute>() == true -> NavigationItem.FAMILY
        destination?.hasRoute<ProfileRoute>() == true -> NavigationItem.PROFILE
        else -> null
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (selectedItem != null) {
                MainNavigationBar(
                    selectedItem = selectedItem,
                    onItemClick = { item ->
                        // Tapping the active tab from a deeper screen goes back to its main screen
                        if (item == NavigationItem.FAMILY && destination?.hasRoute<CircleRoute>() == true) {
                            navController.popBackStack<FamilyRoute>(inclusive = false)
                            return@MainNavigationBar
                        }

                        navController.navigate(item.route) {
                            // Keeps a single copy of each tab and its state when switching
                            // The access flow (Iam) is gone after sign in, so Home graph is the root
                            popUpTo<HomeNavGraphRoute> {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = IamNavGraphRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            iamNavGraph(navController)
            homeNavGraph(navController)
            checkInNavGraph(navController)
            alertsAndSafetyNavGraph(navController)
            socialCompanionshipNavGraph(navController)
            familyNavGraph(navController)
            composable<ProfileRoute> {
                Text(text = "Profile Screen")
            }
        }
    }
}

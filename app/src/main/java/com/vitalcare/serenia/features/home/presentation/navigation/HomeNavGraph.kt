package com.vitalcare.serenia.features.home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.checkin.presentation.navigation.CheckInSkippedRoute
import com.vitalcare.serenia.features.checkin.presentation.navigation.CheckInThanksRoute
import com.vitalcare.serenia.features.home.presentation.home.HomeScreen

@Serializable
data object HomeNavGraphRoute

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    navigation<HomeNavGraphRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onMoodSelected = { mood ->
                    navController.navigate(CheckInThanksRoute(mood = mood.name))
                },
                onSkip = {
                    navController.navigate(CheckInSkippedRoute)
                },
                onHelpClick = { }
            )
        }
    }
}

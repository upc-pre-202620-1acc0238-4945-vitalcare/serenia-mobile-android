package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.dailycheckin.domain.Mood
import com.vitalcare.serenia.features.home.presentation.HomeNavGraphRoute
import com.vitalcare.serenia.features.home.presentation.HomeViewModel
import com.vitalcare.serenia.features.home.presentation.HomeRoute

@Serializable
data object CheckInTimeRoute

@Serializable
data class CheckInThanksRoute(val mood: String)

@Serializable
data object CheckInSkippedRoute

fun NavGraphBuilder.checkInNavGraph(navController: NavController) {

    composable<CheckInTimeRoute> {
        CheckInTimeScreen(
            onContinue = {
                navController.navigate(HomeNavGraphRoute) {
                    popUpTo<CheckInTimeRoute> { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

    composable<CheckInThanksRoute> { backStackEntry ->
        val route: CheckInThanksRoute = backStackEntry.toRoute()
        CheckInThanksScreen(mood = Mood.valueOf(route.mood)) {
            navController.popBackStack<HomeRoute>(inclusive = false)
        }
    }

    composable<CheckInSkippedRoute> {
        CheckInSkippedScreen(
            onResumeCheckIn = {
                navController.getBackStackEntry<HomeRoute>()
                    .savedStateHandle[HomeViewModel.RESUME_CHECK_IN_KEY] = true
                navController.popBackStack<HomeRoute>(inclusive = false)
            },
            onBackToHome = {
                navController.popBackStack<HomeRoute>(inclusive = false)
            }
        )
    }
}

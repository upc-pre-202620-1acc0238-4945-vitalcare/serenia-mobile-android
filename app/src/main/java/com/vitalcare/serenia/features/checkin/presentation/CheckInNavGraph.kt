package com.vitalcare.serenia.features.checkin.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.checkin.domain.Mood
import com.vitalcare.serenia.features.checkin.presentation.skipped.CheckInSkippedScreen
import com.vitalcare.serenia.features.checkin.presentation.thanks.CheckInThanksScreen
import com.vitalcare.serenia.features.home.presentation.home.HomeViewModel
import com.vitalcare.serenia.features.home.presentation.navigation.HomeRoute

@Serializable
data class CheckInThanksRoute(val mood: String)

@Serializable
data object CheckInSkippedRoute

fun NavGraphBuilder.checkInNavGraph(navController: NavController) {

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

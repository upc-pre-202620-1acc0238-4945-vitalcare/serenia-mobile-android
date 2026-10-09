package com.vitalcare.serenia.features.home.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.alertsandsafety.presentation.navigation.AlertSentRoute
import com.vitalcare.serenia.features.checkin.presentation.navigation.CheckInSkippedRoute
import com.vitalcare.serenia.features.checkin.presentation.navigation.CheckInThanksRoute
import com.vitalcare.serenia.features.home.presentation.home.HomeScreen
import com.vitalcare.serenia.features.home.presentation.home.HomeViewModel

@Serializable
data object HomeNavGraphRoute

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    navigation<HomeNavGraphRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> { backStackEntry ->
            val viewModel: HomeViewModel = hiltViewModel()

            // Result sent by the check-in flow when the user decides to answer after a pause
            val savedStateHandle = backStackEntry.savedStateHandle
            val shouldResumeCheckIn by savedStateHandle
                .getStateFlow(HomeViewModel.RESUME_CHECK_IN_KEY, false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(shouldResumeCheckIn) {
                if (shouldResumeCheckIn) {
                    viewModel.resumeCheckIn()
                    savedStateHandle[HomeViewModel.RESUME_CHECK_IN_KEY] = false
                }
            }

            HomeScreen(
                viewModel = viewModel,
                onMoodSelected = { mood ->
                    navController.navigate(CheckInThanksRoute(mood = mood.name))
                },
                onSkip = {
                    navController.navigate(CheckInSkippedRoute)
                },
                onHelpClick = {
                    navController.navigate(AlertSentRoute)
                }
            )
        }
    }
}

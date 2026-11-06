package com.vitalcare.serenia.features.alertsandsafety.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.home.presentation.HomeRoute

@Serializable
data class HelpSentRoute(val outcome: String)

fun NavGraphBuilder.alertsAndSafetyNavGraph(navController: NavController) {

    composable<HelpSentRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<HelpSentRoute>()

        HelpSentScreen(
            outcome = HelpOutcome.valueOf(route.outcome),
            onBackToHome = {
                navController.popBackStack<HomeRoute>(inclusive = false)
            }
        )
    }
}

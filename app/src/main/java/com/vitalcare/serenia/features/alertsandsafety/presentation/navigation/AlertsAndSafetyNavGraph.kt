package com.vitalcare.serenia.features.alertsandsafety.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.alertsandsafety.presentation.alertsent.AlertSentScreen
import com.vitalcare.serenia.features.home.presentation.navigation.HomeRoute

@Serializable
data object AlertSentRoute

fun NavGraphBuilder.alertsAndSafetyNavGraph(navController: NavController) {

    composable<AlertSentRoute> {
        AlertSentScreen {
            navController.popBackStack<HomeRoute>(inclusive = false)
        }
    }
}

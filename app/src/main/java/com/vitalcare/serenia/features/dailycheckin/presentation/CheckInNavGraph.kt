package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.carecircle.presentation.InvitationStepRoute

@Serializable
data object CheckInTimeRoute

fun NavGraphBuilder.checkInNavGraph(navController: NavController) {

    composable<CheckInTimeRoute> {
        CheckInTimeScreen(
            onContinue = {
                navController.navigate(InvitationStepRoute) { launchSingleTop = true }
            }
        )
    }
}

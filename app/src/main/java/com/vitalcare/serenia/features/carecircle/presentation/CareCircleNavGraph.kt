package com.vitalcare.serenia.features.carecircle.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.dailycheckin.presentation.CheckInTimeRoute
import com.vitalcare.serenia.features.home.presentation.HomeNavGraphRoute

@Serializable
data object InvitationStepRoute

@Serializable
data object CircleRoute

fun NavGraphBuilder.careCircleNavGraph(navController: NavController) {

    composable<InvitationStepRoute> {
        InvitationStepScreen(
            onBack = { navController.popBackStack() },
            onEnter = {
                navController.navigate(HomeNavGraphRoute) {
                    popUpTo<CheckInTimeRoute> { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

    composable<CircleRoute> {
        CircleScreen(
            onBack = { navController.popBackStack() }
        )
    }
}

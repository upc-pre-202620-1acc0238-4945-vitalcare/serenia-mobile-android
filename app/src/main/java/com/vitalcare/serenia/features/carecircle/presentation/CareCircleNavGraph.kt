package com.vitalcare.serenia.features.carecircle.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CircleRoute

fun NavGraphBuilder.careCircleNavGraph(navController: NavController) {

    composable<CircleRoute> {
        CircleScreen(
            onBack = {
                navController.popBackStack()
            }
        )
    }
}

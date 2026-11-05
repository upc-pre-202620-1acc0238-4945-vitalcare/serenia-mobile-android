package com.vitalcare.serenia.features.home.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object HomeNavGraphRoute

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    navigation<HomeNavGraphRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen()
        }
    }
}

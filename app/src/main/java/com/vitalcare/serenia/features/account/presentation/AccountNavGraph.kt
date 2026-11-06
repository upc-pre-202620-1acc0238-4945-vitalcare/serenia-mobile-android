package com.vitalcare.serenia.features.account.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object AccountNavGraphRoute

@Serializable
data object AccountRoute

fun NavGraphBuilder.accountNavGraph(navController: NavController) {

    navigation<AccountNavGraphRoute>(startDestination = AccountRoute) {
        composable<AccountRoute> {
            AccountScreen()
        }
    }
}

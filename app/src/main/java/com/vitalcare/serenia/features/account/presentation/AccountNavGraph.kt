package com.vitalcare.serenia.features.account.presentation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object AccountNavGraphRoute

@Serializable
data object AccountRoute

@Serializable
data object ProfileDataRoute

@Serializable
data object ChangePasswordRoute

private const val MESSAGE_KEY = "message"

fun NavGraphBuilder.accountNavGraph(navController: NavController) {

    val goBackWithMessage: (String) -> Unit = { message ->
        navController.previousBackStackEntry?.savedStateHandle?.set(MESSAGE_KEY, message)
        navController.popBackStack()
    }

    navigation<AccountNavGraphRoute>(startDestination = AccountRoute) {
        composable<AccountRoute> { backStackEntry ->
            val message by backStackEntry.savedStateHandle
                .getStateFlow<String?>(MESSAGE_KEY, null)
                .collectAsStateWithLifecycle()

            AccountScreen(
                message = message,
                onMessageShown = { backStackEntry.savedStateHandle[MESSAGE_KEY] = null },
                onEditProfile = {
                    navController.navigate(ProfileDataRoute) { launchSingleTop = true }
                },
                onChangePassword = {
                    navController.navigate(ChangePasswordRoute) { launchSingleTop = true }
                }
            )
        }

        composable<ProfileDataRoute> {
            ProfileDataScreen(
                onBack = { navController.popBackStack() },
                onSaved = { goBackWithMessage("Listo, guardamos tus datos.") }
            )
        }

        composable<ChangePasswordRoute> {
            ChangePasswordScreen(
                onBack = { navController.popBackStack() },
                onSaved = { goBackWithMessage("Listo, tu contraseña cambió.") }
            )
        }
    }
}

package com.vitalcare.serenia.main

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.R
import com.vitalcare.serenia.core.designsystems.accountCircle
import com.vitalcare.serenia.core.designsystems.eventAvailable
import com.vitalcare.serenia.core.designsystems.family
import com.vitalcare.serenia.core.designsystems.home
import com.vitalcare.serenia.features.home.presentation.HomeNavGraphRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.FamilyNavGraphRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.SocialCompanionshipNavGraphRoute

enum class NavigationItem(
    val route: @Serializable Any,
    val icon: ImageVector,
    val title: Int
) {
    HOME(
        route = HomeNavGraphRoute,
        icon = home,
        title = R.string.tab_home
    ),
    REMINDERS(
        route = SocialCompanionshipNavGraphRoute,
        icon = eventAvailable,
        title = R.string.tab_reminders
    ),
    FAMILY(
        route = FamilyNavGraphRoute,
        icon = family,
        title = R.string.tab_family
    ),
    PROFILE(
        route = ProfileRoute,
        icon = accountCircle,
        title = R.string.tab_profile
    )
}

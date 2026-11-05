package com.vitalcare.serenia.features.home.presentation

data class HomeUiState(
    val date: String = "",
    val greeting: String = "",
    val firstName: String? = null,
    val simplifiedMode: Boolean = false
) {
    val title: String
        get() = if (firstName.isNullOrBlank()) greeting else "$greeting, $firstName"
}

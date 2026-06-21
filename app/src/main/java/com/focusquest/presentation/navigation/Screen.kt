package com.focusquest.presentation.navigation

sealed class Screen(val route: String) {
    object Battle : Screen("battle")
    object Victory : Screen("victory")
    object Stats : Screen("stats")
}
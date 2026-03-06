package com.enigma.kitchenorders.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object OrderEntry : Screen("order_entry")
    object Kitchen : Screen("kitchen")
    object Settings : Screen("settings")
    object MenuManagement : Screen("menu_management")
}

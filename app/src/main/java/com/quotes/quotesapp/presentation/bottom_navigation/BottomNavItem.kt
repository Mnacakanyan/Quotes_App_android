package com.quotes.quotesapp.presentation.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    data object Favorites : BottomNavItem("favorites", Icons.Filled.Favorite, "Favorites")
    data object Settings : BottomNavItem("settings", Icons.Filled.Settings, "Settings")
}
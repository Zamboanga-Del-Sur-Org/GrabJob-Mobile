package com.dev.grabjob.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Services : NavigationItem("services", Icons.Default.Build, "Services")
    object Bookings : NavigationItem("bookings", Icons.Default.DateRange, "Bookings")
    object Messages : NavigationItem("messages", Icons.Default.Message, "Messages")
    object Profile : NavigationItem("profile", Icons.Default.Person, "Profile")
}

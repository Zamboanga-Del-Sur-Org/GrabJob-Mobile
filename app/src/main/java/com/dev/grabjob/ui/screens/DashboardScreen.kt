package com.dev.grabjob.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.dev.grabjob.navigation.NavigationItem
import com.dev.grabjob.ui.screens.home.HomeScreen
import com.dev.grabjob.ui.screens.services.ServicesScreen
import com.dev.grabjob.ui.screens.bookings.BookingsScreen
import com.dev.grabjob.ui.screens.messages.MessagesScreen
import com.dev.grabjob.ui.screens.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Services,
        NavigationItem.Bookings,
        NavigationItem.Messages,
        NavigationItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationItem.Home.route) { HomeScreen() }
            composable(NavigationItem.Services.route) { ServicesScreen() }
            composable(NavigationItem.Bookings.route) { BookingsScreen() }
            composable(NavigationItem.Messages.route) { MessagesScreen() }
            composable(NavigationItem.Profile.route) { ProfileScreen() }
        }
    }
}

package com.dev.grabjob.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val services = listOf(
        "Plumbing" to Icons.Default.Build,
        "Electrical" to Icons.Default.ElectricBolt,
        "Carpentry" to Icons.Default.Handyman,
        "Painting" to Icons.Default.Brush,
        "Cleaning" to Icons.Default.CleaningServices
    )

    val recentBookings = listOf(
        "Pipe Repair" to "John Smith",
        "Electrical Wiring" to "Emma Wilson",
        "Wall Painting" to "Mike Johnson"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GrabJob") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                // Search Bar
                SearchBar(
                    query = "",
                    onQueryChange = { },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search for services...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                ) { }
            }

            item {
                Text(
                    "Popular Services",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(services) { (service, icon) ->
                        ServiceCard(service, icon)
                    }
                }
            }

            item {
                Text(
                    "Recent Bookings",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(recentBookings) { (service, provider) ->
                BookingItem(service, provider)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(service: String, icon: ImageVector) {
    Card(
        onClick = { /* TODO */ },
        modifier = Modifier
            .size(120.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = service,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                service,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BookingItem(service: String, provider: String) {
    ListItem(
        headlineContent = { Text(service) },
        supportingContent = { Text(provider) },
        leadingContent = {
            Icon(
                Icons.Default.Assignment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            TextButton(onClick = { /* TODO */ }) {
                Text("View")
            }
        }
    )
}

package com.focusquest.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun FocusQuestNavHost(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Only show bottom bar on Battle and Stats (not Victory)
            if (currentRoute == Screen.Battle.route || currentRoute == Screen.Stats.route) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Battle") },
                        label = { Text("Battle") },
                        selected = currentRoute == Screen.Battle.route,
                        onClick = {
                            if (currentRoute != Screen.Battle.route) {
                                navController.navigate(Screen.Battle.route) {
                                    popUpTo(Screen.Battle.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.BarChart, contentDescription = "Stats") },
                        label = { Text("Stats") },
                        selected = currentRoute == Screen.Stats.route,
                        onClick = {
                            if (currentRoute != Screen.Stats.route) {
                                navController.navigate(Screen.Stats.route) {
                                    popUpTo(Screen.Battle.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Battle.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Battle.route) {
                // Placeholder — will be replaced with BattleScreen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚔️ Focus Quest",
                        style = androidx.compose.material3.MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            composable(Screen.Victory.route) {
                // Placeholder — will be replaced with VictoryScreen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎉 Victory!",
                        style = androidx.compose.material3.MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            composable(Screen.Stats.route) {
                // Placeholder — will be replaced with StatsScreen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📊 Stats",
                        style = androidx.compose.material3.MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
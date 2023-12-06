package com.ddanilov.workshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ddanilov.workshop.screens.details.RandomItemDetailScreen
import com.ddanilov.workshop.screens.home.HomeScreen
import com.ddanilov.workshop.ui.theme.WorkShopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            WorkShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable(route = "home") {
                            HomeScreen(
                                onNavigateDetails = { navController.navigate("details") }
                            )
                        }

                        composable(route = "details") {
                            RandomItemDetailScreen()
                        }
                    }
                }
            }
        }
    }
}

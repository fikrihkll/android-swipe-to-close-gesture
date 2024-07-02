@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.molt.dragtoclosecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.molt.dragtoclosecompose.ui.theme.DragToCloseComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            DragToCloseComposeTheme {
                MainNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = "home") {
            composable(
                route = "home",
            ) {
                HomeScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    onNavigateToDetail = {
                        navController.navigate("detail")
                    }
                )
            }
            composable(route = "detail") {
                DetailScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    onNavigatePop = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

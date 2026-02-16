package com.example.cameracolorpickercompose.navGraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cameracolorpickercompose.screens.CameraScreen
import com.example.cameracolorpickercompose.screens.HistoryScreen
import com.example.cameracolorpickercompose.screens.SplashScreen

@Composable
fun NavGraph(modifier: Modifier, paddingValues: PaddingValues) {
    val controller = rememberNavController()
    NavHost(
//        modifier = modifier,
        navController = controller,
        startDestination = Screens.Splash.route,
        enterTransition = { fadeIn(animationSpec = tween(100)) },
        exitTransition = { fadeOut(animationSpec = tween(100)) },
//        popEnterTransition = { /*under the hood this uses enterTransition, thus no need to manually set this*/ } ,
//        popExitTransition = { /*under the hood this uses exitTransition, thus no need to manually set this*/ }
//        sizeTransform = null
    ) {
        composable(Screens.Splash.route) {
            SplashScreen(
                modifier.padding(paddingValues),
                navigateNext = {
                    controller.navigate(Screens.Camera.route) {
                        popUpTo(Screens.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screens.Camera.route) {
            CameraScreen(
//                modifier,
                paddingValues,
                navigateToHistory = {
                    controller.navigate(Screens.History.route)
                }
            )
//            CardBorderEffect()
        }
        composable(Screens.History.route) {
            HistoryScreen(
                modifier
                    .background(Color.White)
                    .padding(paddingValues),
                onNavigateBack = {
                    controller.popBackStack()
                }
            )
        }
    }
}
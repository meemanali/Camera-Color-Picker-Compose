package com.example.cameracolorpickercompose.navGraph

sealed class Screens(val route: String) {
    data object Splash : Screens("splash")
    data object Camera : Screens("camera")
    data object History : Screens("history")
}

//sealed interface Screens {
//    val route: String
//
//    data object Splash : Screens {
//        override val route = "splash"
//    }
//
//    data object Camera : Screens {
//        override val route = "camera"
//    }
//
//    data object History : Screens {
//        override val route = "history"
//    }
//}
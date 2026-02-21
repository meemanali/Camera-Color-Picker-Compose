package com.example.cameracolorpickercompose.screens

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cameracolorpickercompose.R
import com.example.cameracolorpickercompose.utils.MyImage
import com.example.cameracolorpickercompose.utils.PrimaryButton
import com.example.cameracolorpickercompose.utils.goToSettings
import com.example.cameracolorpickercompose.utils.hexToColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateNext: () -> Unit
) {

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val context = LocalContext.current

    // Track if the permission request has been processed after user interaction
    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
//    var permissionRequestCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(cameraPermissionState.status) {
        if (hasRequestedPermission && cameraPermissionState.status is PermissionStatus.Granted) {
//            navController.navigate(Screens.Camera.route)
            navigateNext()
        }
    }

    MyImage(Modifier.fillMaxSize(), id = R.drawable.img_bg_splash)

    Box(
        modifier = modifier
//            .background(
//                painter = painterResource(R.drawable.img_bg_splash),
//                contentScale = ContentScale.Crop
//            )
//            Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFFE0F7FF), Color(0xFFADD9FF))
//                )
//            )
//            .background(Color.White)
    ) {
//        Image(
//            painter = painterResource(R.drawable.img_bg_splash),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
        LottieAnimation(
            composition = rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.camera_lottie)
            ).value,
//            iterations = LottieConstants.IterateForever,
            speed = 0.5f,
            modifier = Modifier
                .padding(
                    bottom = 36.dp
                ) // if we give first size and then give padding, then the compose will minus the padding from the size thus give padding initial then give size
                .size(300.dp)
                .align(Alignment.Center)
        )
        Text(
            "PrismaCam",
            modifier = Modifier
                .padding(top = 148.dp)
                .align(Alignment.Center)
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = DEFAULT_COLORS
//                    )
//                )
            ,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf("#2664EB".hexToColor(), "#5EA5FA".hexToColor()),
//                    tileMode = TileMode.Repeated,
//                    start = Offset(0f, 0f),
//                    end = Offset(100f, 0f) // adjust width of gradient
                )
            )
        )
        Text(
            "Capture Color",
            modifier = Modifier
                .padding(top = 196.dp)
                .align(Alignment.Center),
            fontSize = 14.sp,
            color = Color.Gray
        )
        // Padding does NOT increase size — it consumes space from the parent.
        PrimaryButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
//                .width(200.dp) // 200dp is rendering a very small width composable
                .padding(bottom = 36.dp, start = 64.dp, end = 64.dp),
//                .clickable {
////                    navController.navigate(Screens.Camera.route)
//                },
            onClick = {

                val status = cameraPermissionState.status
                Log.d(
                    "permTest",
                    "SplashScreen: click: $status"
                ) // it was giving perm denied and show rational as false. On the first permission request  PermissionStatus.Denied.shouldShowRationale == false. This does NOT mean “permanently denied”. It means: Permission has not been requested yet, OR User denied once and selected “Don’t ask again”
                when {
                    status is PermissionStatus.Granted -> {
//                        navController.navigate(Screens.Camera.route)
                        navigateNext()
                    }

                    //                    status is PermissionStatus.Denied &&
                    //                            status.shouldShowRationale -> {
                    //                        cameraPermissionState.launchPermissionRequest()
                    //                        permissionRequestCompleted = true
                    //                    }

                    // first time, thus show system dialog
                    status is PermissionStatus.Denied && !hasRequestedPermission -> { // is PermissionStatus.Denied if status.shouldShowRationale this shit is only available after kotlin 2.2: PermissionStatus.Denied if !hasRequestedPermission
                        cameraPermissionState.launchPermissionRequest()
                        hasRequestedPermission = true
                        //                        permissionRequestCompleted = true
                    }

                    // user denied once, show rationale UI or retry
                    status is PermissionStatus.Denied && status.shouldShowRationale -> {
                        cameraPermissionState.launchPermissionRequest()
                        hasRequestedPermission = true
                    }

                    else -> {
                        context.goToSettings()
                        hasRequestedPermission = true
                        //                        permissionRequestCompleted = true
                    }
                }
//                navController.navigate(Screens.Camera.route)
            }
        ) {
            MyImage(
                Modifier.size(22.dp),
                id = R.drawable.ic_camera,
//                size = 22
            )
            Text(
                "Start Camera",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
//                    .width(200.dp)
//                    .fillMaxWidth()
//                    .padding(horizontal = 32.dp)
            )
        }
    }
}

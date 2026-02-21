package com.example.cameracolorpickercompose.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.DrawableRes
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.cameracolorpickercompose.R
import com.example.cameracolorpickercompose.dialogs.ExitConfirmationDialog
import com.example.cameracolorpickercompose.ui.theme.GrayTrans75
import com.example.cameracolorpickercompose.ui.theme.GrayTrans95
import com.example.cameracolorpickercompose.utils.MarginHorizontal
import com.example.cameracolorpickercompose.utils.MyImage
import com.example.cameracolorpickercompose.utils.access
import com.example.cameracolorpickercompose.utils.toArgbHex
import com.example.cameracolorpickercompose.utils.yuvToColor
import com.example.cameracolorpickercompose.vms.AddColorViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

//@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraScreen(
//    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: AddColorViewModel = koinViewModel(),
    navigateToHistory: () -> Unit
) {

    Log.d("compTest", "CameraScreen: ")

    //    var latestCapturedColor by remember { mutableStateOf(Color.Transparent) }
    val latestCapturedColor by viewModel.latestCapturedColor.collectAsState()

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) } // LENS_FACING_UNKNOWN
    var torchEnabled by remember { mutableStateOf(false) }

    var centerColor by remember { mutableStateOf(Color.Transparent) }

    var showExitDialog by rememberSaveable { mutableStateOf(false) }

    @DrawableRes
    var icFlash by remember { mutableIntStateOf(R.drawable.ic_flash_off) }
//    @DrawableRes var icCamera by remember { mutableIntStateOf(R.drawable.ic_swipe_camera) }

    val activity = LocalActivity.current

//    val hazeState = rememberHazeState()

    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
//            .hazeSource(hazeState)
    ) {
        CameraPreview(
            modifier = Modifier
                .fillMaxSize(),
//                .hazeSource(hazeState, 0f),
            lensFacing = lensFacing,
            torchEnabled = torchEnabled,
            onCenterColorSampled = {
                centerColor = it
            }
//            onFocusTap = { focusTap ->
//
//            }
        )

        // Crosshair
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2
            val cy = size.height / 2
            drawLine(Color.Black, Offset(cx - 20, cy), Offset(cx + 20, cy), 4f)
            drawLine(Color.Black, Offset(cx, cy - 20), Offset(cx, cy + 20), 4f)
        }

        MyImage(
            Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 42.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                ) // giving padding before the size and then giving padding again for inner content
                .size(42.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .clickable {
                    if (torchEnabled) {
                        torchEnabled = false
                        icFlash = R.drawable.ic_flash_off
                    } else {
                        torchEnabled = true
                        icFlash = R.drawable.ic_flash_on
                    }
                }
                .padding(12.dp),
            icFlash,
            scale = ContentScale.Fit,
//            size = 24
        )

        MyImage(
            Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 42.dp,
                    end = 24.dp,
                    start = 24.dp,
                    bottom = 24.dp
                )
                .size(42.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .clickable {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK)
                            CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                }
                .padding(12.dp),
            R.drawable.ic_swipe_camera,
            scale = ContentScale.Fit,
//            size = 24
        )

//        Box(
//            Modifier
//                .fillMaxWidth()
//                .height(120.dp)
//                .align(Alignment.BottomCenter)
//                .background(Color(0xBF888888), RoundedCornerShape(12.dp, 12.dp))
////                .blur(100.dp)
//        ) {
//
//        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
//                .padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter)
                    .padding(paddingValues),
            // modifier as we r passing an external modifier which may already contains constraints eg padding, offsets, align etc. Modifier.align() does NOT override earlier layout constraints. thus wcu then here. Note: when we were giving that modifier to BottomActionBar, then it was working. but when we gave it to the column, it stopped working. may b that was also taking full width but as wh set align contentAlignment = Alignment.BottomCenter in BottomActionBar, that's why it was only showing content on bottom.
            // thus now we are directly passing those paddingValues
            horizontalAlignment = Alignment.CenterHorizontally // for setting the row center
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .background(
                        color = Color(0xCC2B2B2B), // semi-transparent dark bg
                        shape = CircleShape // RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 7.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(centerColor, shape = CircleShape)
                )

                MarginHorizontal(8)

                Text(
                    text = centerColor.toArgbHex(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            BottomActionBar(
                modifier = Modifier.fillMaxWidth(),
                latestCapturedColor,
                onHistoryClick = navigateToHistory,
                onPickClick = {
                    viewModel.saveColor(centerColor)
                }
            )
        }
    }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirm = {
                activity?.finish() // LocalActivity.current?.finish() current can only be called within a compose context
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier,
    lensFacing: Int,
    torchEnabled: Boolean,
    onCenterColorSampled: (Color) -> Unit
//    onFocusTap: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnCenterColorSampled by rememberUpdatedState(onCenterColorSampled)

    val surfaceRequests = remember { MutableStateFlow<SurfaceRequest?>(null) }
    val surfaceRequest by surfaceRequests.collectAsState(null)

//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//    var cameraProvider: ProcessCameraProvider? = remember { null }
    var camera by remember { mutableStateOf<Camera?>(null) }

    val analysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(
                    ContextCompat.getMainExecutor(context)
                ) { image ->
                    val color = image.yuvToColor(image.width / 2, image.height / 2)
                    currentOnCenterColorSampled(color)
                    image.close()
                }
            }
    }

    val preview = remember {
        Preview.Builder().build().apply {
            setSurfaceProvider { request ->
                surfaceRequests.value = request
            }
        }
    }


    LaunchedEffect(lensFacing) {
        val provider = ProcessCameraProvider.awaitInstance(context)
        val selector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        provider.unbindAll()
        camera = provider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            analysis
        )
    }
    
    LaunchedEffect(torchEnabled) {
        camera?.cameraControl?.enableTorch(torchEnabled)
    }

    // State to track focus indicator
    var focusPoint by remember { mutableStateOf<Offset?>(null) }
    var showFocusIndicator by remember { mutableStateOf(false) }

    // Coordinate transformer: Compose UI → Camera surface
//    val coordinateTransformer = remember { MutableCoordinateTransformer() }

    Box(modifier = modifier) {
        surfaceRequest?.access {
            CameraXViewfinder(
                surfaceRequest = this,
                modifier = modifier
                    .pointerInput(camera) {
                        // Tap-to-focus
                        detectTapGestures { offset ->

                            Log.d("CameraFocus", "Tap detected at: ${offset.x}, ${offset.y}")

                            // Show focus indicator at tap location
                            focusPoint = offset
                            showFocusIndicator = true

                            val cam = camera ?: return@detectTapGestures

                            val width = size.width.toFloat()
                            val height = size.height.toFloat()

                            val meteringFactory = SurfaceOrientedMeteringPointFactory(
                                width,
                                height
                            )

                            val focusPoint = meteringFactory.createPoint(
                                offset.x,
                                offset.y
                            )

                            val action = FocusMeteringAction
                                .Builder(
                                    focusPoint,
                                    FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
                                )
                                .setAutoCancelDuration(3, TimeUnit.SECONDS)
                                .build()

                            val canFocus = camera?.cameraInfo?.isFocusMeteringSupported(action)
                            Log.d("CameraFocus", "Focus supported: $canFocus")

                            cam.cameraControl
                                .startFocusAndMetering(action)
                                .addListener(
                                    {
                                        Log.d("CameraFocus", "Focus metering completed")

                                        // Hide indicator after focus completes
                                        showFocusIndicator = false

//                                    onFocusTap(true)
                                    },
                                    ContextCompat.getMainExecutor(context)
                                )
                        }
                    }
                    .pointerInput(camera) {
                        // Pinch-to-zoom
                        detectTransformGestures { _, _, zoom, _ ->
                            val cam = camera ?: return@detectTransformGestures
                            val zoomState =
                                cam.cameraInfo.zoomState.value ?: return@detectTransformGestures

                            val newRatio = (zoomState.zoomRatio * zoom).coerceIn(
                                zoomState.minZoomRatio,
                                zoomState.maxZoomRatio
                            )

                            cam.cameraControl.setZoomRatio(newRatio)
                        }
                    }
//                .pointerInput(Unit) {  // Using Unit means it never restarts
//                    // If camera changes, this still uses old camera reference!
//                }
            )
        }

        // Focus indicator overlay
        if (showFocusIndicator && focusPoint != null) {
            FocusIndicatorSquare(
                position = focusPoint!!, //  ?: Offset(0f, 0f)
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// Alternative: Square/Rectangle Indicator (iOS style)
@Composable
fun FocusIndicatorSquare(
    position: Offset,
    modifier: Modifier = Modifier
) {
    val scale by rememberInfiniteTransition(label = "focus_scale").animateFloat(
        initialValue = 1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Canvas(modifier = modifier) {
        val indicatorSize = 70.dp.toPx()
        val scaledSize = indicatorSize * scale

        // Draw rounded rectangle
        drawRoundRect(
            color = Color.DarkGray,
            topLeft = Offset(
                position.x - scaledSize / 2,
                position.y - scaledSize / 2
            ),
            size = Size(scaledSize, scaledSize),
            cornerRadius = CornerRadius(8.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun BottomActionBar(
    modifier: Modifier,
    latestCapturedColor: Color,
    onHistoryClick: () -> Unit,
    onPickClick: () -> Unit
//    onColorClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {

        // Background bar
        Card(
            modifier = Modifier
                .fillMaxWidth() // .fillMaxWidth(0.9f)
//                .wrapContentHeight()
                .padding(bottom = 0.dp),
//                .padding(vertical = 7.dp)
//                .height(84.dp),
            shape = RoundedCornerShape(16.dp, 16.dp),
//            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = GrayTrans75 // brownish background
            ),
            border = BorderStroke(0.8.dp, GrayTrans95)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 36.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Absolute.SpaceBetween ignore RTL
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Column(
                    Modifier
//                        .size(48.dp)
                        .wrapContentSize()
                        .clickable(onClick = onHistoryClick),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF5E7BBE), shape = CircleShape)
                            .border(1.dp, Color(0xF3888888), shape = CircleShape)
                            .padding(8.dp)
//                            .size(20.dp)
                        ,
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        "History",
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

//                MarginHorizontal(60)

                // Color button
                Column(
                    Modifier
//                        .size(48.dp)
                        .wrapContentSize()
                        .clickable(onClick = onHistoryClick),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(latestCapturedColor, shape = CircleShape)
                            .border(1.dp, Color(0xF3888888), shape = CircleShape)
                            .padding(8.dp),
//                            .size(20.dp)
//                        imageVector = Icons.Default.History,
//                        contentDescription = null,
//                        tint = Color.White
                    )
                    Text(
                        latestCapturedColor.toArgbHex(), // latestCapturedColor.toArgb().toString() // prints negative number
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }

        // Center floating button
        CircleButton(
            modifier = Modifier
//                .align(Alignment.Center)
                .size(64.dp)
                .offset(y = (-44).dp) // 32 for half + as we have given vertical = 8.dp in above row
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .padding(3.dp),
            icon = Icons.Default.Colorize,
//            size = 60.dp,
            background = Color.White,
            iconTint = Color.Black,
            elevation = 3.dp,
//            border = BorderStroke(3.dp, Color.LightGray),
            onClick = onPickClick
        )
    }
}

@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
//    size: Dp = 48.dp,
    background: Color,
    iconTint: Color = Color.White,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
//            .size(size)
            .clickable(onClick = onClick),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = background),
        border = border
    ) {
        icon?.access {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                imageVector = this,
                contentDescription = null,
                tint = iconTint
            )
        }
    }
}

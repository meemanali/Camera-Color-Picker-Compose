package com.example.cameracolorpickercompose.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.cameracolorpickercompose.ui.theme.PrimaryColor
import com.example.cameracolorpickercompose.ui.theme.PrimaryColorSecond
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

val DEFAULT_COLORS = listOf(PrimaryColor, PrimaryColorSecond)

fun Modifier.bgRoundedGradient(
    colors: List<Color> = DEFAULT_COLORS,
    roundness: Int = 10
): Modifier {
    return background(Brush.linearGradient(colors), shape = RoundedCornerShape(roundness.dp))
}

fun Color.toArgbHex(): String {
    return String.format("#%08X", this.toArgb())
}

//fun String.toColor(): Color {
//    return try {
//        Color(toColorInt())
//    } catch (_: Exception) {
//        Color.Transparent
//    }
//}

//inline val String.hexToColor
//    get() = Color(this.toColorInt())

/**
 * In Compose:
 *
 * Calling this repeatedly in recompositions can be expensive
 *
 * If used in a composable, remember it:
 *
 * val color = remember(hex) { Color(hex.toColorInt()) }*/
fun String.hexToColor(): Color {
    return Color(this.toColorInt())
}

fun Dp.toPx(density: Density): Float = this.value * density.density

/**Conceptually, Button is implemented roughly like this:
@Composable
fun Button(
...
content: @Composable RowScope.() -> Unit
)
So internally, a Row is used to lay out the content.*/
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit // @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier
            .height(56.dp)
            .bgRoundedGradient(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
//        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        content()
    }
}

/** can b used in columns*/
@Composable
fun MarginVertical(margin: Int) {
    Spacer(Modifier.height(margin.dp))
}

/** can b used in rows */
@Composable
fun MarginHorizontal(margin: Int) {
    Spacer(Modifier.width(margin.dp))
}

fun withRow(block: RowScope.() -> Unit) {}

@Composable
fun MyImage(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    scale: ContentScale = ContentScale.Crop,
//    size: Int = -1,
    roundness: Int = -1
) {
    Image(
        painter = painterResource(id),
        contentDescription = null,
        contentScale = scale,
        modifier = modifier
//            .let {
//                if (size == -1) it.fillMaxSize()
//                else it.size(size.dp)
//            }
            .let {
                if (roundness != -1) it.clip(RoundedCornerShape(roundness.dp))
                else it
            }
    )
}

@OptIn(ExperimentalContracts::class)
inline fun <T> T.access(block: T.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
}

//fun <T> T?.orReturn(): T & Any {
//    return this ?: return Unit
//}

//@Suppress("UNCHECKED_CAST")
//@OptIn(ExperimentalContracts::class)
//inline fun <reified T> T?.orReturn(): T {
//    contract {
//        returns() implies (this@orReturn != null)
//    }
//    return this ?: return
//}

//fun <T, R : T> T?.or(returnValue: R): T {
//    return this ?: return returnValue
//}

//@OptIn(ExperimentalContracts::class)
//inline fun <T> T?.orReturn(block: () -> Nothing): T {
//    contract {
//        returns() implies (this@orReturn != null)
//    }
//    return this ?: block()
//}

fun Context.goToSettings() {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(
                "package",
                packageName,
                null
            )
        }
    startActivity(intent)
}

//fun ImageProxy.getCenterPixelColor(): Color {
//    val yBuffer = planes[0].buffer
//    val uBuffer = planes[1].buffer
//    val vBuffer = planes[2].buffer
//
//    val y = yBuffer.get(0).toInt() and 0xFF
//    val u = uBuffer.get(0).toInt() and 0xFF
//    val v = vBuffer.get(0).toInt() and 0xFF
//
//    val r = (y + 1.370705f * (v - 128)).coerceIn(0f, 255f)
//    val g = (y - 0.337633f * (u - 128) - 0.698001f * (v - 128)).coerceIn(0f, 255f)
//    val b = (y + 1.732446f * (u - 128)).coerceIn(0f, 255f)
//
//    return Color(
//        red = r / 255f,
//        green = g / 255f,
//        blue = b / 255f
//    )
//}

fun ImageProxy.yuvToColor(x: Int, y: Int): Color {
    val yPlane = this.planes[0]
    val uPlane = this.planes[1]
    val vPlane = this.planes[2]
    val yBuffer = yPlane.buffer
    val uBuffer = uPlane.buffer
    val vBuffer = vPlane.buffer
    val yRowStride = yPlane.rowStride
    val uvRowStride = uPlane.rowStride
    val uvPixelStride = uPlane.pixelStride
    val yValue = yBuffer.get(yRowStride * y + x).toInt() and 0xFF
    val uvOffset =
        uvRowStride * (y / 2) + uvPixelStride * (x / 2)
    val uValue = uBuffer.get(uvOffset).toInt() and 0xFF
    val vValue = vBuffer.get(uvOffset).toInt() and 0xFF

    val yF = yValue - 16
    val uF = uValue - 128
    val vF = vValue - 128
    val r = (1.164f * yF + 1.596f * vF).toInt().coerceIn(0, 255)
    val g = (1.164f * yF - 0.813f * vF - 0.391f * uF).toInt().coerceIn(0, 255)
    val b = (1.164f * yF + 2.018f * uF).toInt().coerceIn(0, 255)
    val argb = android.graphics.Color.argb(255, r, g, b)

    return Color(argb)
}

internal suspend inline fun runOnUi(crossinline block: suspend CoroutineScope.() -> Unit) {
    withContext(Dispatchers.Main) {
        // `this` is a CoroutineScope. thus the coro scope the block() will get is tied to Main
        block.invoke(this) // or
//        this.block()
    }
}

suspend inline fun <T> runOnIo(crossinline block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO) {
        return@withContext block.invoke(this)
    }
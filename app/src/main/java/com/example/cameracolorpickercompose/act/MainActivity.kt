package com.example.cameracolorpickercompose.act

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.cameracolorpickercompose.navGraph.NavGraph
import com.example.cameracolorpickercompose.ui.theme.CameraColorPickerComposeTheme
import com.example.cameracolorpickercompose.utils.access

/**
 * remember saves state during recomposition
 * rememberSaveAble saves state during config changes
 *      but for using this, the data we save must be serializable as it saves the data into some kind of disk and retain again after config changes. eg wcu bitmaps in rememberSaveAble as it implements parcelable. but there r other things which r not serializable eg exoplayer thus we can't use exo in rem save able
 * thus the latest approach for that is using retain. it can restore from config changes and the objects it stores does not need to be serializable. they can b any data type
 *
 * the retain api also provides a new effect handler called RetainedEffect (available from compose bom 2025.12.00). similar as DisposableEffect
 * // will be called when composable leaves composition
 *     DisposableEffect(true) {
 *          onDispose {
 *
 *          }
 *      }
 * // will be called when composable leaves composition, but will not be called when composable leaves for config changes
 *      RetainedEffect(true) {
 *          onRetire {
 *
 *          }
 *      }
 *
 * in simple it is similar as saving saving exo instance in viewmodel
 * but with architecture pov it is better, as wc test these composables without needing vm
 *
 * retain won't survive process death. rememberSavable can. for surviving PD, ycu saveStateHandler combo with vm
 */

/**
 * mob dev news oct 25
 * cmp for web is in beta now
 * kotlin to swift export
 * display build files in module. each module has diff gradle files. thus by using this latest AS narwhal, wc select this option from more in files tree mode
 * diff layout size checking for preview composables
 * side loading will not effect AS builds as it uses adb under the hood and that is allowed
 * ggl is planning to introduce a new type of account with the help of which we will b able to share apk to a limited num of people
 *
 *
 * inlining:
 * inline fun knows about context from where they r being called. eg if we call a fun inside a coro, we will not be able to call suspend fun. but if we call inline fun, it will. as it knows the code will b copy pasted there.
 * wca use reified in generic fun using inline. thus compiler knows the class name / type of that generic T
 * inline fun can also b used for allowing, disallowing non local return
 * wca make classes inline. ie value classes
 *
 *
 * use navigateUp as that is aware of the current stack. but the popBackStack is not
 *
 *
 *
 * wcu profiler in AS to monitor cpu usage, method calls usage etc. select on tract java/kotlin method, then select a laps of usage
 * wca heap dump on memory and thus check which object has taken most memory
 * similarly wca check battery usage
 * wca completely monitor network performance using AS network monitor. it was part of profiler but now it showed with db inspector. aca check all the headers of incoming/outgoing requests
 *
 *
 *
 * if we just need a pic captured by user, then no need to use camera and camera perm, just use act launcher as there will be many camera apps within the device which captures the image and return img
 */

/**
 * All ViewModel Scoping Options in Compose
 * 1️⃣ Navigation Destination Scope (Your Current Setup)
 *
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window?.access {
            WindowCompat.setDecorFitsSystemWindows(this, false)
            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars =
                true
        }
        setContent {
            CameraColorPickerComposeTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { paddingValues ->
                    NavGraph(
                        modifier = Modifier
                            .fillMaxSize()
//                            .padding(paddingValues)
                        ,
                        paddingValues
                    )
                }
            }
        }
    }
}
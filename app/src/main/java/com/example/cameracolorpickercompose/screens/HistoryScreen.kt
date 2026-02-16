package com.example.cameracolorpickercompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cameracolorpickercompose.utils.MarginHorizontal
import com.example.cameracolorpickercompose.utils.MarginVertical
import com.example.cameracolorpickercompose.utils.hexToColor
import com.example.cameracolorpickercompose.vms.ColorListViewModel
import org.koin.androidx.compose.koinViewModel

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: ColorListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {

    val colors by viewModel.colors.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
//            .background(Color.White)
            .fillMaxSize()
//            .background(Color.White)
//            .padding(16.dp)
//        , verticalArrangement = Arrangement.Center // makes the top bar and lazy grid items too in the center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            MarginHorizontal(3)

            Text(
                text = "Saved Colors",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (colors.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Take all remaining space
            ) {
                Text(
                    text = "No colors saved yet",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center), // perfectly centered
                    textAlign = TextAlign.Center
                )
            }
//            Text(
//                text = "No colors saved yet",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 200.dp)
//                    .align(Alignment.CenterHorizontally),
//                textAlign = TextAlign.Center
//            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 96.dp),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(colors, key = { it._id.toHexString() }) { colorItem ->
                    ColorTile(
                        hexCode = colorItem.hexCode,
                        onDelete = { viewModel.deleteColor(colorItem._id) }
                    )
                }
            }
        }

        MarginVertical(5)
    }
}

//private val PentagonShape = GenericShape { size, _ ->
//    val w = size.width
//    val h = size.height
//    moveTo(w / 2f, 0f)
//    lineTo(w, h * 0.38f)
//    lineTo(w * 0.82f, h)
//    lineTo(w * 0.18f, h)
//    lineTo(0f, h * 0.38f)
//    close()
//}

//val SoftHexagon = GenericShape { size, _ ->
//    val w = size.width
//    val h = size.height
//    val r = 16.dp.toPx(LocalDensity.current) // corner rounding
//
//    moveTo(w / 2, 0f + r)
//    lineTo(w - r, h * 0.25f)
//    lineTo(w - r, h * 0.75f)
//    lineTo(w / 2, h - r)
//    lineTo(r, h * 0.75f)
//    lineTo(r, h * 0.25f)
//    close()
//}

//@Composable
//fun SoftHexagonShape(cornerRadius: Dp = 16.dp): GenericShape {
//    val rPx = with(LocalDensity.current) { cornerRadius.toPx() }
//
//    return GenericShape { size, _ ->
//        val w = size.width
//        val h = size.height
//
//        moveTo(w / 2f, 0f + rPx)
//        lineTo(w - rPx, h * 0.25f)
//        lineTo(w - rPx, h * 0.75f)
//        lineTo(w / 2f, h - rPx)
//        lineTo(rPx, h * 0.75f)
//        lineTo(rPx, h * 0.25f)
//        close()
//    }
//}

@Composable
fun ColorTile(
    hexCode: String,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
//            .clip(PentagonShape)
//            .clip(SoftHexagonShape())
//            .clip(RoundedCornerShape(16.dp))
            .shadow(3.dp, RoundedCornerShape(12.dp))
            .background(hexCode.hexToColor())
//            .clickable { /* future: preview */ }
//            .animateItemPlacement()
    ) {
        // HEX text overlay
        Text(
            text = hexCode,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )

        // Delete icon
        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Close,
                contentDescription = "Delete",
                tint = Color.White,
            )
        }
    }
}

//@Composable
//fun ColorCard(hexCode: String, onDelete: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(60.dp)
//                    .background(
//                        color = hexCode.hexToColor(),
//                        shape = RoundedCornerShape(8.dp)
//                    )
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Text(
//                text = hexCode,
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.weight(1f)
//            )
//
//            IconButton(onClick = onDelete) {
//                Icon(Icons.Default.Delete, contentDescription = "Delete")
//            }
//        }
//    }
//}
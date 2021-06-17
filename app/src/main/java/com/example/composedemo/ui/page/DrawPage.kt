package com.example.composedemo.ui.page

import android.view.MotionEvent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composedemo.R
import com.example.composedemo.common.topBar
import com.example.composedemo.ui.MainActions
import com.example.composedemo.ui.draw.*
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun DrawPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {

    Scaffold(
        modifier = modifier,
        topBar = {
            topBar(
                title = stringResource(id = R.string.draw_Layout),
                click = { actions.upPress() })
        },
        content = { MyContent() }
    )
}

@ExperimentalFoundationApi
@Composable
private fun MyContent() {
    var position by remember { mutableStateOf(0) }
    val titleList by lazy {
        mutableListOf(
            "画手势" to R.mipmap.ic_mine1,
            "画直线" to R.mipmap.ic_mine2,
            "画圆" to R.mipmap.ic_mine3,
            "画矩形" to R.mipmap.ic_mine4,
            "画箭头" to R.mipmap.ic_mine5,
            "画线段" to R.mipmap.ic_mine5
        )
    }
    Column {
        when (position) {
            0 -> {
                DrawGesture(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
            1 -> {
                StraightLine(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
            2 -> {
                DrawCircle(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
            3->{
                DrawRect(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
            4->{
                DrawArrow(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
            5->{
                DrawLineSegment(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = colorResource(id = R.color.main_text)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            titleList.forEachIndexed { index, pair ->
                DrawTab(
                    selected = index == position,
                    indexc = index,
                    content = pair
                ) {
                    if (it != position)
                        position = it
                }
            }
        }
    }
}

@Composable
private fun DrawTab(
    selected: Boolean,
    content: Pair<String, Int>,
    indexc: Int,
    onClick: (i: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp, 0.dp)
            .clickable { onClick(indexc) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = content.second),
            contentDescription = "",
            tint = if (selected) Color.White else Color.Black
        )
        Text(
            text = content.first,
            color = if (selected) Color.White else Color.Black
        )
    }
}


@Composable
private fun drawLine(modifier: Modifier = Modifier.fillMaxWidth()) {


    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // 蓝色的线
        drawLine(
            start = Offset(100f, 100f),
            end = Offset(w - 100f, 100f),
            color = Color.Blue,
            strokeWidth = 10f,
            cap = StrokeCap.Square,
            alpha = 1f
        )

        // 渐变的线
        drawLine(
            brush = Brush.linearGradient(
                0.0f to Color.Red,
                0.3f to Color.Green,
                1.0f to Color.Blue,
                start = Offset(100f, 150f),
                end = Offset(w - 100f, 150f),
                tileMode = TileMode.Repeated
            ),
            start = Offset(100f, 150f),
            end = Offset(w - 100f, 150f),
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            alpha = 1f
        )

        // 横向渐变的线
        drawLine(
            brush = Brush.horizontalGradient(
                0.0f to Color.Red,
                0.3f to Color.Green,
                1.0f to Color.Blue,
                startX = 100f,
                endX = w - 100f,
                tileMode = TileMode.Mirror
            ),
            start = Offset(100f, 200f),
            end = Offset(w - 100f, 200f),
            strokeWidth = 10f,
            cap = StrokeCap.Butt,
            alpha = 1f
        )

        // 竖直渐变的线
        drawLine(
            brush = Brush.verticalGradient(
                0.0f to Color.Red,
                0.3f to Color.Green,
                1.0f to Color.Blue,
                startY = 250f,
                endY = 500f,
                tileMode = TileMode.Clamp
            ),
            start = Offset(100f, 250f),
            end = Offset(100f, 500f),
            strokeWidth = 20f,
            cap = StrokeCap.Round,
            alpha = 1f
        )

        // 横向渐变的有 pathEffect.dashPathEffect的虚线
        drawLine(
            brush = Brush.horizontalGradient(
                0.0f to Color.Red,
                0.5f to Color.Yellow,
                1.0f to Color.Blue,
                startX = 100f,
                endX = w - 100f,
                tileMode = TileMode.Clamp
            ),
            start = Offset(100f, 550f),
            end = Offset(w - 100f, 550f),
            strokeWidth = 10f,
            cap = StrokeCap.Butt,
            alpha = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(100f, 20f), 10f)
        )
    }
}

@Composable
private fun drawRect(modifier: Modifier = Modifier.fillMaxWidth()) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRect(
            brush = Brush.horizontalGradient(
                0.0f to Color.Red,
                0.5f to Color.Yellow,
                1.0f to Color.Blue,
                startX = 100f,
                endX = w - 100f,
                tileMode = TileMode.Clamp
            ),
            topLeft = Offset(100f, 600f),
            size = Size(1000f, 300f),
            /*FloatRange(from = 0.0, to = 1.0)*/
            alpha = 1.0f,
            style = Fill,
            blendMode = DrawScope.DefaultBlendMode
        )
    }
}

@Composable
private fun drawRoundRect(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            brush = Brush.horizontalGradient(
                0.0f to Color.Red,
                0.5f to Color.Yellow,
                1.0f to Color.Blue,
                startX = 100f,
                endX = w - 100f,
                tileMode = TileMode.Clamp
            ),
            topLeft = Offset(100f, 1000f),
            size = Size(1000f, 300f),
            cornerRadius = CornerRadius(100f, 100f),
            /*@FloatRange(from = 0.0, to = 1.0)*/
            alpha = 1.0f,
            style = Fill,
            blendMode = DefaultBlendMode
        )
    }
}

@Composable
private fun drawImage(modifier: Modifier = Modifier.fillMaxWidth()) {
    Canvas(modifier = Modifier.fillMaxWidth()) {
        val w = size.width
        val h = size.height
        drawImage(
            image = ImageBitmap(
                width = 300,
                height = 300,
                config = ImageBitmapConfig.Argb8888,
                hasAlpha = true,
                colorSpace = ColorSpaces.Srgb
            ),
            topLeft = Offset(100f, 1400f),
            alpha = 1.0f,
            style = Fill,
            blendMode = DefaultBlendMode
        )
    }
}


@Composable
fun clearDialog(onDismiss: () -> Unit = {}, onClick: () -> Unit = {}) {
    AlertDialog(
        onDismissRequest = {},
        text = {
            Text(
                text = "确定要撤销所有吗？",
                color = Color.Black,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        buttons = {
            Row(modifier = Modifier.fillMaxWidth()) {
                dialogButton(
                    Modifier.weight(1f),
                    "取消",
                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text_light))
                ) { onDismiss() }
                dialogButton(
                    Modifier.weight(1f),
                    "确定",
                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text))
                ) { onClick() }
            }
        }
    )
}

package com.example.composedemo.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composedemo.R
import com.example.composedemo.common.topBar
import com.example.composedemo.ui.draw.MyRowV4
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
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
//                MyRow()
                MyCanvas()
            }
        }
    )
}

@Composable
private fun MyRow() {
    MyRowV4(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Blue)
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                .weight(4f)
                .background(Red)
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                .weight(1f)
                .background(Green)
        )
    }
}

@Composable
private fun MyCanvas() {
    LazyColumn {
        item{  Text(text = "asddddddddddddddddddddddddddddddddddd") }


    }




//                drawRect 画矩形
//                drawRoundRect 画圆角矩形
//                drawImage 绘制图片
//                drawCircle 画圆形
//                drawOval 画椭圆形
//                drawArc 画弧度跟扇形
//                drawPath 画路径
//                drawPoints 画点


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








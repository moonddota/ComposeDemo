package com.example.composedemo.ui.draw

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.example.composedemo.ui.page.clearDialog

@ExperimentalFoundationApi
@Composable
fun StraightLine(modifier: Modifier) {

    Column(modifier = modifier) {
        val pathList by remember { mutableStateOf(mutableListOf<Path>()) }
        val pathList1 by remember { mutableStateOf(mutableListOf<Path>()) }
        var point: Poibt? = null
        var action by remember { mutableStateOf(-99999999) }
        var isShowDiakog by remember { mutableStateOf(false) }


        if (isShowDiakog) {
            clearDialog(onDismiss = {
                isShowDiakog = false
            }, onClick = {
                pathList.clear()
                isShowDiakog = false
            })
        }

        Text(
            text = "回撤",
            modifier = Modifier.combinedClickable(
                onLongClick = { if (pathList.isNotEmpty()) isShowDiakog = true },
                onClick = { if (pathList.isNotEmpty()) pathList.remove(pathList.last()) })
        )

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        point = Poibt(it.x, it.y)
                    }
                    MotionEvent.ACTION_UP -> {
                        pathList1.clear()
                        pathList.add(Path().apply {
                            moveTo(point!!.x, point!!.y)
                            lineTo(it.x, it.y)
                        })
                        action += 1
                    }
                    MotionEvent.ACTION_MOVE -> {
                        pathList1.clear()
                        pathList1.add(
                            Path().apply {
                                moveTo(point!!.x, point!!.y)
                                lineTo(it.x, it.y)
                            }
                        )
                        action += 1
                    }
                }
                true
            }
        ) {

            val w = size.width
            val h = size.height
            action.let {
                pathList.forEach {
                    drawPath(
                        path = it,
                        color = Color.Red,
                        alpha = 1f,
                        style = Stroke(10f)
                    )
                }
                pathList1.forEach {
                    drawPath(
                        path = it,
                        color = Color.Red,
                        alpha = 1f,
                        style = Stroke(10f)
                    )
                }
            }
        }
    }


//    //画线
//    drawLine()
//
//    //画矩形
//    drawRect()
//
//    //画圆角矩形
//    drawRoundRect()
//
//    //绘制图片
//    drawImage()
//                drawCircle 画圆形
//                drawOval 画椭圆形
//                drawArc 画弧度跟扇形
//                drawPath 画路径
//                drawPoints 画点


}


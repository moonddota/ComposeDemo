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
fun DrawLineSegment(modifier: Modifier) {

    var startPoint: Poibt? = null
    val list = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    val list1 = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    var action by remember { mutableStateOf(-999999999) }

    val awrad = Math.atan(90.0) // 箭头角度
    val arraow_len: Double = 10.0 / 2 * 3
    var a = 10

    Column(modifier = modifier) {
        var isShowDiakog by remember { mutableStateOf(false) }

        if (isShowDiakog) {
            clearDialog(onDismiss = {
                isShowDiakog = false
            }, onClick = {
                list.clear()
                isShowDiakog = false
            })
        }

        Text(
            text = "回撤",
            modifier = Modifier.combinedClickable(
                onLongClick = {
                    if (list.isNotEmpty()) isShowDiakog = true
                },
                onClick = {
                    if (list.isNotEmpty()) list.remove(list.last())
                })
        )

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startPoint = Poibt(it)
                    }
                    MotionEvent.ACTION_UP -> {
                        list1.clear()
                        list.add(startPoint!!.getNewPoint() to Poibt(it))
                        action += 1
                    }
                    MotionEvent.ACTION_MOVE -> {
                        list1.clear()
                        list1.add(startPoint!!.getNewPoint() to Poibt(it))
                        action += 1
                    }
                }
                true
            }) {
            action.let {
                list1.forEach {
                    drawPath(
                        path = getLineSegmentPath(a, awrad, arraow_len, it),
                        color = Color.Red,
                        alpha = 1f,
                        style = Stroke(10f)
                    )
                }
                list.forEach {
                    drawPath(
                        path = getLineSegmentPath(a, awrad, arraow_len, it),
                        color = Color.Red,
                        alpha = 1f,
                        style = Stroke(10f)
                    )
                }
            }

        }
    }

}


private fun getLineSegmentPath(
    a: Int,
    awrad: Double,
    arraow_len: Double,
    pointPair: Pair<Poibt, Poibt>
): Path {
    val path = Path()
    path.moveTo(pointPair.first.x, pointPair.first.y)
    path.lineTo(pointPair.second.x, pointPair.second.y)

    val arrXY_1: DoubleArray = rotateVec(
        pointPair.second.x - pointPair.first.x,
        pointPair.second.y - pointPair.first.y,
        awrad,
        true,
        arraow_len
    )
    val arrXY_2: DoubleArray = rotateVec(
        pointPair.second.x - pointPair.first.x,
        pointPair.second.y - pointPair.first.y,
        -awrad,
        true,
        arraow_len
    )
    path.moveTo(
        (pointPair.second.x - arrXY_1[0]).toFloat(),
        (pointPair.second.y - arrXY_1[1]).toFloat()
    )
    path.lineTo(
        (pointPair.second.x - arrXY_2[0]).toFloat(),
        (pointPair.second.y - arrXY_2[1]).toFloat()
    )
    path.moveTo(
        (pointPair.first.x - arrXY_1[0]).toFloat(),
        (pointPair.first.y - arrXY_1[1]).toFloat()
    )
    path.lineTo(
        (pointPair.first.x - arrXY_2[0]).toFloat(),
        (pointPair.first.y - arrXY_2[1]).toFloat()
    )

    for (j in 1 until a) {
        val mx: Float = ((a - j) * pointPair.first.x + j * pointPair.second.x) / a
        val my: Float = ((a - j) * pointPair.first.y + j * pointPair.second.y) / a
        val x_1 = (mx - arrXY_1[0]).toFloat()
        val y_1 = (my - arrXY_1[1]).toFloat()
        val x_2 = (mx - arrXY_2[0]).toFloat()
        val y_2 = (my - arrXY_2[1]).toFloat()
        path.moveTo(x_1, y_1)
        path.lineTo(x_2, y_2)
    }

    return path
}
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
fun DrawArrow(modifier: Modifier) {

    var startPoint: Poibt? = null
    val list = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    val list1 = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    var action by remember { mutableStateOf(-999999999) }

    val arrowSize: Float = 20f
    val H = arrowSize.toDouble() // 箭头高度
    val L = (arrowSize / 2).toDouble() // 底边的一�?

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
            }
        ) {

            action.let {
                list1.forEach {
                    drawPath(
                        path = getDrawArrowPath(L, H, it),
                        color = Color.Red,
                        alpha = 1f,
                        style = Stroke(10f)
                    )
                }
                list.forEach {
                    drawPath(
                        path = getDrawArrowPath(L, H, it),
                        color = Color.Red,
                        alpha = 1f,
                        style = Stroke(10f)
                    )
                }
            }
        }
    }
}

private fun getDrawArrowPath(L: Double, H: Double, PointPair: Pair<Poibt, Poibt>): Path {
    var awrad = Math.atan(L / 2 / H) // 箭头角度
    var arraow_len = Math.sqrt(L / 2 * L / 2 + H * H) - 5 // 箭头的长�?
    var arrXY_1: DoubleArray = rotateVec(
        PointPair.second.x - PointPair.first.x,
        PointPair.second.y - PointPair.first.y,
        awrad,
        true,
        arraow_len
    )
    var arrXY_2: DoubleArray = rotateVec(
        PointPair.second.x - PointPair.first.x,
        PointPair.second.y - PointPair.first.y,
        -awrad,
        true,
        arraow_len
    )
    var x_3 = (PointPair.second.x - arrXY_1[0]).toFloat() // (x3,y3)是第�?端点
    var y_3 = (PointPair.second.y - arrXY_1[1]).toFloat()
    var x_4 = (PointPair.second.x - arrXY_2[0]).toFloat() // (x4,y4)是第二端�?
    var y_4 = (PointPair.second.y - arrXY_2[1]).toFloat()

    val path = Path()
    // 画线
    path.moveTo(PointPair.first.x, PointPair.first.y)
    path.lineTo(x_3, y_3)
    path.lineTo(x_4, y_4)
    path.close()

    awrad = Math.atan(L / H) // 箭头角度

    arraow_len = Math.sqrt(L * L + H * H) // 箭头的长�?

    arrXY_1 = rotateVec(
        PointPair.second.x - PointPair.first.x,
        PointPair.second.y - PointPair.first.y,
        awrad,
        true,
        arraow_len
    )
    arrXY_2 = rotateVec(
        PointPair.second.x - PointPair.first.x,
        PointPair.second.y - PointPair.first.y,
        -awrad,
        true,
        arraow_len
    )
    x_3 = (PointPair.second.x - arrXY_1[0]).toFloat() // (x3,y3)是第�?端点
    y_3 = (PointPair.second.y - arrXY_1[1]).toFloat()
    x_4 = (PointPair.second.x - arrXY_2[0]).toFloat() // (x4,y4)是第二端�?
    y_4 = (PointPair.second.y - arrXY_2[1]).toFloat()

    val mArrowTrianglePath = Path()

    mArrowTrianglePath.reset()
    mArrowTrianglePath.moveTo(PointPair.second.x, PointPair.second.y)
    mArrowTrianglePath.lineTo(x_4, y_4)
    mArrowTrianglePath.lineTo(x_3, y_3)
    mArrowTrianglePath.close()
    path.addPath(mArrowTrianglePath)
    return path
}

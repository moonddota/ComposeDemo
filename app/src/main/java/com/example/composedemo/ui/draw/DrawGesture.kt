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
fun DrawGesture(modifier: Modifier) {
    Column(modifier = modifier) {
        val pathList by remember { mutableStateOf(mutableListOf<Path>()) }
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
                        pathList.add(Path().apply { moveTo(it.x, it.y) })
                        action += 1
                    }
                    MotionEvent.ACTION_UP -> {
                    }
                    MotionEvent.ACTION_MOVE -> {
                        pathList
                            .last()
                            .lineTo(it.x, it.y)
                        action += 1
                    }
                    else -> false
                }
                true
            }
        ) {
            action.let {
                pathList.forEach {
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
}
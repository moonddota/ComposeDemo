package com.example.composedemo.ui.draw

import android.util.Log
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
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.example.composedemo.ui.page.clearDialog

@ExperimentalFoundationApi
@Composable
fun DrawCircle(modifier: Modifier) {
    var action by remember { mutableStateOf(-999999999) }
    val radiusList = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    val radiusList1 = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    var center: Poibt? = null

    Column(modifier = modifier) {
        var isShowDiakog by remember { mutableStateOf(false) }

        if (isShowDiakog) {
            clearDialog(onDismiss = {
                isShowDiakog = false
            }, onClick = {
                radiusList.clear()
                isShowDiakog = false
            })
        }

        Text(
            text = "回撤",
            modifier = Modifier.combinedClickable(
                onLongClick = {
                    if (radiusList.isNotEmpty()) isShowDiakog = true
                },
                onClick = {
                    if (radiusList.isNotEmpty()) radiusList.remove(radiusList.last())
                })
        )

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        center = Poibt(it)
                    }
                    MotionEvent.ACTION_UP -> {
                        radiusList1.clear()
                        radiusList.add(center!!.getNewPoint() to Poibt(it))
                        action += 1
                    }
                    MotionEvent.ACTION_MOVE -> {
                        radiusList1.clear()
                        radiusList1.add(center!!.getNewPoint() to Poibt(it))
                        action += 1
                    }
                }
                true
            }) {

            action.let {
                radiusList.forEach {
                    drawCircle(
                        color = Color.Red,
                        radius = it.first.twoPointSpacing(it.second),
                        center = it.first.getOffset(),
                        /*@FloatRange(from = 0.0, to = 1.0)*/
                        alpha = 1.0f,
                        style = Stroke(width = 8f),
                        blendMode = DefaultBlendMode
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = 8f,
                        center = it.first.getOffset(),
                        /*@FloatRange(from = 0.0, to = 1.0)*/
                        alpha = 1.0f,
                        style = Fill,
                        blendMode = DefaultBlendMode
                    )
                }
                radiusList1.forEach {
                    drawCircle(
                        color = Color.Red,
                        radius = it.first.twoPointSpacing(it.second),
                        center = it.first.getOffset(),
                        /*@FloatRange(from = 0.0, to = 1.0)*/
                        alpha = 1.0f,
                        style = Stroke(width = 8f),
                        blendMode = DefaultBlendMode
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = 8f,
                        center = it.first.getOffset(),
                        /*@FloatRange(from = 0.0, to = 1.0)*/
                        alpha = 1.0f,
                        style = Fill,
                        blendMode = DefaultBlendMode
                    )
                }
            }
        }
    }

}
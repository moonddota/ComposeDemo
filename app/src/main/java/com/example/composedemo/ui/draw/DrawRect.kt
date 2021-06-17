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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.example.composedemo.ui.page.clearDialog

@ExperimentalFoundationApi
@Composable
fun DrawRect(modifier: Modifier) {
    var startPoint: Poibt? = null
    val list = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    val list1 = remember { mutableListOf<Pair<Poibt, Poibt>>() }
    var action by remember { mutableStateOf(-999999999) }

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
                    drawRect(
                        color = Color.Red,
                        topLeft = it.first.getRectTopLeft(it.second),
                        size = it.first.getRectSize(it.second),
                        style = Stroke(width = 8f)
                    )
                }
                list.forEach {
                    drawRect(
                        color = Color.Red,
                        topLeft = it.first.getRectTopLeft(it.second),
                        size = it.first.getRectSize(it.second),
                        style = Stroke(width = 8f)
                    )
                }
            }
        }

    }

}
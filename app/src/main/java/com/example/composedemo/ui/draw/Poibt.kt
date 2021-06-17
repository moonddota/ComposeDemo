package com.example.composedemo.ui.draw

import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import java.lang.Math.pow
import java.lang.Math.sqrt


class Poibt() {

    var x: Float = 0f
    var y: Float = 0f

    constructor(x: Float, y: Float) : this() {
        this.x = x
        this.y = y
    }

    constructor(m: MotionEvent) : this() {
        this.x = m.x
        this.y = m.y
    }

}


fun Poibt.getOffset(): Offset = Offset(this.x, this.y)

fun Poibt.getNewPoint(): Poibt = Poibt(this.x, this.y)

/**
 * 两点间距
 * （x1-x2）平方 +（y1-y2）平方  最后的结果开平方
 */
fun Poibt.twoPointSpacing(a: Poibt): Float =
    sqrt(pow((this.x - a.x).toDouble(), 2.0) + pow((this.y - a.y).toDouble(), 2.0)).toFloat()

/**
 * 获取矩形的Size
 * 取两点的 x、y 差值得绝对值
 */
fun Poibt.getRectSize(a: Poibt): Size =
    Size(Math.abs(this.x - a.x), Math.abs(this.y - a.y))

/**
 * 获取矩形的左上角点
 */
fun Poibt.getRectTopLeft(a: Poibt): Offset =
    if (this.x < a.x && this.y < a.y) {
        this.getOffset()
    } else if (this.x < a.x && this.y >= a.y) {
        Offset(this.x, a.y)
    } else if (this.x >= a.x && this.y < a.y) {
        Offset(a.x, this.y)
    } else {
        a.getOffset()
    }




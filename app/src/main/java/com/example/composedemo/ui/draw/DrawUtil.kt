package com.example.composedemo.ui.draw

fun rotateVec(
    px: Float, py: Float, ang: Double,
    isChLen: Boolean, newLen: Double
): DoubleArray {
    val mathstr = DoubleArray(2)
    // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度�?�新长度
    var vx = px * Math.cos(ang) - py * Math.sin(ang)
    var vy = px * Math.sin(ang) + py * Math.cos(ang)
    if (isChLen) {
        val d = Math.sqrt(vx * vx + vy * vy)
        vx = vx / d * newLen
        vy = vy / d * newLen
    }
    mathstr[0] = vx
    mathstr[1] = vy
    return mathstr
}
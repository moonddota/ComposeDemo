package com.example.composedemo.util

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import com.example.composedemo.App
import com.sczhizhou.navpad.util.ScreenInfoUtils.getDensity

/**
 * dp to px
 * @param dpValue 待转换dp单位值[Float]
 * @return 转换后px单位值[Int]
 */
fun dp2px(dpValue: Float): Int =
    (dpValue * getDensity(App.instance) + 0.5F).toInt()

/**
 * px to dp
 * @param pxValue 待转换px单位值[Float]
 * @return 转换后dp单位值[Int]
 */
fun px2dp(pxValue: Float): Int =
    (pxValue / getDensity(App.instance) + 0.5F).toInt()


fun px2sp(pxValue: Float): Int {
    val fontScale: Float = App.instance.getResources().getDisplayMetrics().scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

fun sp2px(spValue: Float): Int {
    val fontScale: Float = App.instance.getResources().getDisplayMetrics().scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}


fun expandTouchArea(view: View) {
    expandTouchArea(view, 20)
}

fun expandTouchArea(view: View, size: Int) {
    val parentView = view.parent as View
    parentView.post {
        val rect = Rect()
        view.getHitRect(rect)
        rect.top -= size
        rect.bottom += size
        rect.left -= size
        rect.right += size
        parentView.touchDelegate = TouchDelegate(rect, view)
    }
}
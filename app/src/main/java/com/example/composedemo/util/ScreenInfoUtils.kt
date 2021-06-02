package com.sczhizhou.navpad.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.WindowManager

object ScreenInfoUtils {

    private const val TAG = "ScreenInfoUtils"

// 开发对应标准屏幕尺寸  2560x1440

    /**
     * 获取屏幕高度系数
     * 2392F 为 标准屏幕可用高度
     * @return 当前屏幕可用高度与标准屏幕高度比例
     */
    @JvmStatic
    fun getHeightCoefficient(context: Context): Float {
        return getScreenHeight(context).toFloat() / 2392F
    }

    /**
     * 获取屏幕高度系数
     * 2392F 为 标准屏幕可用宽度
     * @return 当前屏幕可用宽度与标准屏幕宽度比例
     */
    @JvmStatic
    fun getWidthCoefficient(context: Context): Float {
        return getScreenWidth(context).toFloat() / 1440F
    }

    /**
     * Get Screen Width
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    /**
     * Get Screen Height
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }


    /**
     * Get Screen Real Height
     *
     * @param context Context
     * @return Real Height
     */
    @JvmStatic
    fun getRealHeight(context: Context): Int {
        val display = getDisplay(context)
        val dm = DisplayMetrics()
        display.getRealMetrics(dm)
        return dm.heightPixels
    }

    /**
     * Get Screen Real Width
     *
     * @param context Context
     * @return Real Width
     */
    @JvmStatic
    fun getRealWidth(context: Context): Int {
        val display = getDisplay(context)
        val dm = DisplayMetrics()
        display.getRealMetrics(dm)
        return dm.widthPixels
    }

    /**
     * Get StatusBar Height
     */
    @JvmStatic
    fun getStatusBarHeight(mContext: Context): Int {
        val resourceId = mContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            mContext.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    /**
     * Get ActionBar Height
     */
    @JvmStatic
    fun getActionBarHeight(mContext: Context): Int {
        val tv = TypedValue()
        return if (mContext.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, mContext.resources.displayMetrics)
        } else 0
    }

    /**
     * Get NavigationBar Height
     */
    @JvmStatic
    fun getNavigationBarHeight(mContext: Context): Int {
        val resources = mContext.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    /**
     * Get Density
     */
    @JvmStatic
    fun getDensity(context: Context): Float {
        return getDisplayMetrics(context).density
    }

    /**
     * Get Dpi
     */
    @JvmStatic
    fun getDpi(context: Context): Int {
        return getDisplayMetrics(context).densityDpi
    }

    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    @JvmStatic
    fun getDisplay(context: Context): Display {
        val wm = if (context is Activity) {
            context.windowManager
        } else {
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return wm.defaultDisplay
    }

    /**
     * Get DisplayMetrics
     *
     * @param context Context for get Resources
     * @return DisplayMetrics
     */
    @JvmStatic
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }


    /**
     * Get ScreenInfo
     */
    private fun getScreenInfo(context: Context): String {
        return " \n" +
                "--------ScreenInfo--------" + "\n" +
                "Screen Width : " + getScreenWidth(context) + "px\n" +
                "Screen RealWidth :" + getRealWidth(context) + "px\n" +
                "Screen Height: " + getScreenHeight(context) + "px\n" +
                "Screen RealHeight: " + getRealHeight(context) + "px\n" +
                "Screen StatusBar Height: " + getStatusBarHeight(context) + "px\n" +
                "Screen ActionBar Height: " + getActionBarHeight(context) + "px\n" +
                "Screen NavigationBar Height: " + getNavigationBarHeight(context) + "px\n" +
                "Screen Dpi: " + getDpi(context) + "\n" +
                "Screen Density: " + getDensity(context) + "\n" +
                "--------------------------"
    }


    /**
     * Print screenInfo to logcat
     */
    fun printScreenInfo(context: Context) {
        Log.d(TAG, getScreenInfo(context))
    }
}
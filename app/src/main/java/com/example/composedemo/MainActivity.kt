package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.paging.ExperimentalPagingApi
import com.blankj.utilcode.util.BarUtils
import com.example.composedemo.ui.NavGraph
import com.example.composedemo.ui.theme.ComposeDemoTheme
import com.example.composedemo.util.FileUtils.checkDeviceHasNavigationBar
import com.example.composedemo.util.asColor
import com.example.composedemo.util.px2dp

class MainActivity : ComponentActivity() {
    @ExperimentalPagingApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = R.color.main_text.asColor()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val bottomDP =
            if (checkDeviceHasNavigationBar(this))
                px2dp(BarUtils.getNavBarHeight().toFloat()).dp
            else 0.dp
        val modifier = Modifier.padding(0.dp, 0.dp, 0.dp, bottomDP)
        setContent {
            ComposeDemoTheme {
                NavGraph(modifier = modifier)
            }
        }
    }
}





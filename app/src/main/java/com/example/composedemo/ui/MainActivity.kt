package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.view.WindowCompat
import androidx.paging.ExperimentalPagingApi
import com.example.composedemo.ui.NavGraph
import com.example.composedemo.ui.theme.ComposeDemoTheme
import com.example.composedemo.util.asColor

class MainActivity : ComponentActivity() {
    @ExperimentalPagingApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = R.color.main_text.asColor()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ComposeDemoTheme {
                NavGraph()
            }
        }
    }
}





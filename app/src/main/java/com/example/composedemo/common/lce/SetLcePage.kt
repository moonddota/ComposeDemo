package com.example.composedemo.common.lce

import androidx.compose.runtime.Composable
import com.example.composedemo.model.PlayError
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlayState
import com.example.composedemo.model.PlaySuccess

@Composable
fun SetLcePage(playState: PlayState, onErrorClick: () -> Unit, content: @Composable () -> Unit) {

    when (playState) {
        PlayLoading -> {
            LoadingContent()
        }
        is PlayError -> {
            ErrorContent(onErrorClick = onErrorClick)
        }
        is PlaySuccess<*> -> {
            content()
        }
    }

}
package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Queue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun ScoreRankListPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {

    Scaffold(
        modifier = modifier,
        topBar = {
            PlayAppBar(
                title = "我的积分", click = { actions.upPress() },
                showRight = true,
                rightImg = Icons.Default.Queue
            )
        },
        content = {

        }
    )

}
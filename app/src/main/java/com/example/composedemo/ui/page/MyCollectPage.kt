package com.example.composedemo.ui.page

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.common.topBar
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.ui.MainActions
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun MyCollectPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {
    var loadArticleState by remember { mutableStateOf(false) }
    var refreshingState by remember { mutableStateOf(false) }
    val MyCollectList by myViewModel.MyCollectList.observeAsState(PlayLoading)

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.listMyCollect(false)
    }

    Column(modifier = modifier) {
        topBar(
            title = stringResource(id = R.string.mine_collect),
            click = { actions.upPress() })
        SetLcePage(playState = MyCollectList,
            onErrorClick = {
                myViewModel.listMyCollect(false)
            }
        ) {
            val list = (MyCollectList as PlaySuccess<MutableList<ArticleBean>>).data
            SwipeToRefreshAndLoadLayout(
                refreshingState = refreshingState,
                loadState = refreshingState,
                onRefresh = {
                    myViewModel.listMyCollect(false)
                    refreshingState = true
                },
                onLoad = {
                    myViewModel.listMyCollect(true)
                    refreshingState = true
                }
            ) {
                ArticleListPaging(actions, list, myViewModel)
                Log.e("tag","${list?.size?:0}")
                refreshingState = false
            }
        }

    }
}
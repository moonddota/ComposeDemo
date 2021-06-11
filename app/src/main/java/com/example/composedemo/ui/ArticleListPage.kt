package com.example.composedemo.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.common.topBar
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.viewmodel.MyViewModel


@ExperimentalFoundationApi
@Composable
fun ArticleListPage(
    modifier: Modifier,
    id: String,
    title: String,
    actions: MainActions,
    myViewModel: MyViewModel
) {
    var loadArticleState by remember { mutableStateOf(false) }
    val articleList by myViewModel.articleList.observeAsState(PlayLoading)
    var refreshingState by remember { mutableStateOf(false) }

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.getlistArticle(false, id)
    }

    Column(modifier = modifier) {
        topBar(title = title, click = { actions.upPress() })
        SetLcePage(playState = articleList,
            onErrorClick = { myViewModel.getlistArticle(false, id) }
        ) {

            val myList = (articleList as PlaySuccess<MutableList<ArticleBean>>).data

            SwipeToRefreshAndLoadLayout(
                refreshingState = refreshingState,
                loadState = refreshingState,
                onRefresh = {
                    myViewModel.getlistArticle(false, id)
                    refreshingState = true
                },
                onLoad = {
                    myViewModel.getlistArticle(true, id)
                    refreshingState = true
                }
            ) {
                ArticleListPaging(actions, myList, myViewModel)
                Log.e("tag", "${myList?.size ?: 0}")
                refreshingState = false
            }
        }
    }

}
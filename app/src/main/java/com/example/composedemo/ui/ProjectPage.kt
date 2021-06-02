package com.example.composedemo.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blankj.utilcode.util.BarUtils
import com.example.composedemo.R
import com.example.composedemo.bean.ProjectListRes
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.viewmodel.MyViewModel
import com.sczhizhou.navpad.util.px2dp

@ExperimentalFoundationApi
@Composable
fun ProjectPage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {
    var loadArticleState by remember { mutableStateOf(false) }
    val projectTables by myViewModel.projectTabs.observeAsState(PlayLoading)
    val projectList = myViewModel.projectsListData.observeAsState()
    var position by remember { mutableStateOf(0) }
    var id by remember { mutableStateOf("") }
    var refreshingState by remember { mutableStateOf(false) }

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.getProjectTabs()
    }
    SetLcePage(playState = projectTables,
        onErrorClick = {
            myViewModel.getProjectTabs()
        }
    ) {

        val tables = (projectTables as PlaySuccess<List<ProjectListRes>>).data
        if (tables.isNotEmpty()) id = tables[0].id ?: ""

        Column(modifier = modifier) {
            StatusBarHeight()
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.main_text)),
                contentPadding = PaddingValues(5.dp),
            ) {
                items(count = 1) {
                    tables?.forEachIndexed { index, table ->
                        Text(
                            text = table.name ?: "",
                            color = if (position == index) Color.White else Color.Black,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    if (position == index) return@clickable
                                    position = index
                                    id = tables[position].id ?: ""
                                    myViewModel.getListProjects(id, false)
                                })
                    }
                }
            }
            val mList = projectList.value?: mutableListOf()
            SwipeToRefreshAndLoadLayout(
                refreshingState = refreshingState,
                loadState = refreshingState,
                onRefresh = {
                    refreshingState = true
                    myViewModel.getListProjects(id, false)
                },
                onLoad = {
                    refreshingState = true
                    myViewModel.getListProjects(id, true)
                }
            ) {
                Log.e("size", " size     ${mList.size}   id   ${id}")
                ArticleListPaging(actions, mList, myViewModel)
                refreshingState = false
            }
        }
    }
}



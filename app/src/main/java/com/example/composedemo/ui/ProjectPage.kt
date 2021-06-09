package com.example.composedemo.ui

import android.content.res.loader.ResourcesLoader
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.ProjectListRes
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun ProjectPage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {
    var loadArticleState by remember { mutableStateOf(false) }
    val projectTables by myViewModel.projectTabs.observeAsState(PlayLoading)
    val projectList by myViewModel.projectsListData.observeAsState()
    var position by remember { mutableStateOf(0) }
    var itemPair by remember { mutableStateOf(Pair("", "")) }
    var isShowContent by remember { mutableStateOf(true) }
    var refreshingState by remember { mutableStateOf(false) }

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.getProjectTabs()
    }

    val tables = mutableListOf<ProjectListRes>()
    if (projectTables is PlaySuccess<*>) {
        tables.addAll((projectTables as PlaySuccess<List<ProjectListRes>>).data)
        if (tables.isNotEmpty()) itemPair = Pair(tables[0].name ?: "", tables[0].id ?: "")
    }


    SetLcePage(playState = projectTables,
        onErrorClick = {
            myViewModel.getProjectTabs()
        }
    ) {
        Column(modifier = modifier) {
            PlayAppBar(title = itemPair.first,
                showBack = false,
                click = { actions.upPress() },
                showRight = true,
                rightImg = painterResource(id = R.mipmap.ic_up_down),
                rightClick = { isShowContent = !isShowContent })
            if (isShowContent) {
                SwipeToRefreshAndLoadLayout(
                    refreshingState = refreshingState,
                    loadState = refreshingState,
                    onRefresh = {
                        refreshingState = true
                        myViewModel.getListProjects(itemPair.second, false)
                    },
                    onLoad = {
                        refreshingState = true
                        myViewModel.getListProjects(itemPair.second, true)
                    }
                ) {
                    Log.e(
                        "size",
                        " size     ${(projectList ?: mutableListOf()).size}   id   ${itemPair.second}"
                    )
                    ArticleListPaging(actions, projectList ?: mutableListOf(), myViewModel)
                    refreshingState = false
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.main_text)),
                    contentPadding = PaddingValues(5.dp),
                ) {
                    itemsIndexed(tables) { index, table ->
                        Text(
                            text = table.name ?: "",
                            color = if (position == index) Color.White else Color.Black,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable {
                                    if (position != index) {
                                        position = index
                                        itemPair =
                                            Pair(itemPair.first, tables[position].id ?: "")
                                        refreshingState = true
                                        myViewModel.getListProjects(itemPair.second, false)
                                    }
                                    isShowContent = true
                                })
                    }
                }
            }
        }
    }
}

@Composable
fun aaa() {

}



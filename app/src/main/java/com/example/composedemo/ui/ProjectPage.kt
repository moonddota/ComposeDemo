package com.example.composedemo.ui

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
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.bean.ProjectListRes
import com.example.composedemo.common.topBar
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.viewmodel.MyViewModel

private val tables by lazy { mutableListOf<ProjectListRes>() }
private var itemPair = Pair("", "")
private var page = 0

@ExperimentalFoundationApi
@Composable
fun ProjectPage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {
    val projectTables by myViewModel.projectTabs.observeAsState(PlayLoading)
    val projectList by myViewModel.projectsListData.observeAsState()
    val contentList by remember { mutableStateOf(mutableListOf<ArticleBean>()) }

    if (projectTables is PlaySuccess<*>) {
        if (tables.isEmpty()) {
            tables.addAll((projectTables as PlaySuccess<List<ProjectListRes>>).data)
            if (tables.isNotEmpty()) {
                itemPair = Pair(tables[0].name ?: "", tables[0].id ?: "")
            }
        }
    }

    if (projectList != null) {
        if (projectList?.first == false) contentList.clear()
        contentList.addAll(projectList?.second ?: mutableListOf())
    }

    if (tables.isEmpty())
        myViewModel.getProjectTabs()

    SetLcePage(playState = projectTables,
        onErrorClick = {
            myViewModel.getProjectTabs()
        }
    ) {
        ProjectContent(actions, modifier, myViewModel, contentList)
    }
}

@ExperimentalFoundationApi
@Composable
private fun ProjectContent(
    actions: MainActions,
    modifier: Modifier,
    myViewModel: MyViewModel,
    contentList: MutableList<ArticleBean>
) {
    var position by remember { mutableStateOf(0) }
    var isShowContent by remember { mutableStateOf(true) }

    Column(modifier = modifier) {
        topBar(title = itemPair.first,
            showBack = false,
            click = { actions.upPress() },
            showRight = true,
            rightImg = painterResource(id = R.mipmap.ic_up_down),
            rightClick = { isShowContent = !isShowContent })
        if (isShowContent) {
            ProjectList(actions, contentList, myViewModel)
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
                            .fillMaxWidth()
                            .clickable {
                                if (position != index) {
                                    position = index
                                    itemPair = Pair(itemPair.first, tables[position].id ?: "")
                                    contentList.clear()
                                    page = 0
                                    myViewModel.getListProjects(itemPair.second, page, false)
                                }
                                isShowContent = true
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ProjectList(
    actions: MainActions,
    contentList: MutableList<ArticleBean>,
    myViewModel: MyViewModel
) {
    var refreshingState by remember { mutableStateOf(false) }

    SwipeToRefreshAndLoadLayout(
        refreshingState = refreshingState,
        loadState = refreshingState,
        onRefresh = {
            refreshingState = true
            page = 0
            myViewModel.getListProjects(itemPair.second, page, false)
        },
        onLoad = {
            refreshingState = true
            page += 1
            myViewModel.getListProjects(itemPair.second, page, true)
        }
    ) {
        ArticleListPaging(actions, contentList, myViewModel)
        refreshingState = false
    }
}



package com.example.composedemo.ui

import android.text.TextUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.bean.ChildrenBean
import com.example.composedemo.bean.TreeListRes
import com.example.composedemo.util.ResourceUtil
import com.example.composedemo.viewmodel.MyViewModel
import java.util.*


private val SquarePageTagList by lazy { mutableListOf("体系", "导航") }
private val colorList by lazy { arrayListOf<Int>() }
private val random by lazy { Random() }

@ExperimentalFoundationApi
@Composable
fun SquarePage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {
    val position by myViewModel.squarePagePosition.observeAsState()
    val SquareTrees by myViewModel.SquareTrees.observeAsState()
    val SquareNavis by myViewModel.SquareNavis.observeAsState()
    initColors()
    when (position) {
        0 -> {
            if (SquareTrees.isNullOrEmpty()) myViewModel.listTrees()
        }
        1 -> {
            if (SquareNavis.isNullOrEmpty()) myViewModel.listNavis()
        }
    }

    Column(modifier = modifier) {
        StatusBarHeight()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.main_text)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SquarePageTagList.forEachIndexed { index, s ->
                Tab(
                    text = { Text(text = s, fontSize = 16.sp) },
                    selected = position == index,
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Black,
                    modifier = Modifier
                        .width(150.dp)
                        .padding(10.dp, 0.dp),
                    onClick = {
                        myViewModel.changeSquarePagePosition(index = index)
                    }
                )
            }
        }
        when (position) {
            0 -> SquareList(SquareTrees ?: listOf(), actions)
            1 -> SquareList(SquareNavis ?: listOf(), actions)
        }

    }
}

@Composable
fun SquareList(list: List<TreeListRes>, actions: MainActions) {
    LazyColumn {
        items(list) { item ->
            val indexColor = colorList[random.nextInt(colorList.size)]
            val firstName =
                if (TextUtils.isEmpty(item.name ?: "")) ""
                else (item.name ?: "").substring(0, 1)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 20.dp, 10.dp, 5.dp)
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = colorResource(id = indexColor),
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = firstName,
                        color = Color.White,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = item.name ?: "",
                        color = Color(0xff444444),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SquareListChildren(item.children, actions)
                    SquareListArticles(item.articles, actions)
                }
            }
        }
    }
}

@Composable
private fun SquareListChildren(list: List<ChildrenBean>?, actions: MainActions) {
    if (list.isNullOrEmpty()) return
    Column(modifier = Modifier.fillMaxWidth()) {
        var i = 0
        while (i < list.size) {
            if (i + 1 <= list.size - 1) {
                val id1 = list[i].id ?: ""
                val name1 = list[i].name ?: ""
                val id2 = list[i + 1].id ?: ""
                val name2 = list[i + 1].name ?: ""
                Row(verticalAlignment = Alignment.CenterVertically) {
                    childrenBox(
                        name1,
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        actions.jumpArticleList(id1, name1)
                    }
                    childrenBox(
                        name2,
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        actions.jumpArticleList(id2, name2)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                i += 2
            } else {
                val id3 = list[i].id ?: ""
                val name3 = list[i].name ?: ""
                childrenBox(name3, Modifier.align(Alignment.CenterHorizontally)) {
                    actions.jumpArticleList(id3, name3)
                }
                Spacer(modifier = Modifier.height(5.dp))
                i += 1
            }
        }
    }
}

@Composable
private fun SquareListArticles(list: List<ArticleBean>?, actions: MainActions) {
    if (list.isNullOrEmpty()) return
    Column(modifier = Modifier.fillMaxWidth()) {
        var i = 0
        while (i < list.size) {
            if (i + 1 <= list.size - 1) {
                val ab1 = ArticleBean(
                    title = list[i].title,
                    link = list[i].link
                )
                val ab2 = ArticleBean(
                    title = list[i + 1].title,
                    link = list[i + 1].link
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    childrenBox(
                        list[i].title ?: "",
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) { actions.enterArticle(ab1) }
                    childrenBox(
                        list[i + 1].title ?: "",
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) { actions.enterArticle(ab2) }
                }
                Spacer(modifier = Modifier.height(5.dp))
                i += 2
            } else {
                val ab3 = ArticleBean(
                    title = list[i].title,
                    link = list[i].link
                )
                childrenBox(list[i].title ?: "", Modifier.align(Alignment.CenterHorizontally)) {
                    actions.enterArticle(ab3)
                }
                Spacer(modifier = Modifier.height(5.dp))
                i += 1
            }
        }
    }
}

@Composable
private fun childrenBox(s: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp, color = colorResource(id = R.color.main_text))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(modifier = Modifier.padding(8.dp), text = s, color = Color.Black, fontSize = 20.sp)
    }
}

private fun initColors() {
    colorList.clear()
    for (i in 0..18) {
        val resId: Int = ResourceUtil.getResId("in" + (i + 1), R.color::class.java)
        colorList.add(resId)
    }
}



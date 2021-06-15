package com.example.composedemo.ui.page

import android.text.TextUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.composedemo.bean.TreeListRes
import com.example.composedemo.ui.MainActions
import com.example.composedemo.ui.widget.StatusBarHeight
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
            0 -> SquareList(SquareTrees ?: listOf(), 0, actions)
            1 -> SquareList(SquareNavis ?: listOf(), 1, actions)
        }

    }
}

private val colorType0 by lazy { mutableListOf<Int>() }
private val colorType1 by lazy { mutableListOf<Int>() }

@ExperimentalFoundationApi
@Composable
fun SquareList(list: List<TreeListRes>, type: Int, actions: MainActions) {
    LazyColumn {

        list.forEachIndexed { index, bean ->
            when (type) {
                0 -> {
                    if (colorType0.size - 1 < index)
                        colorType0.add(colorList[random.nextInt(colorList.size)])
                }
                1 -> {
                    if (colorType1.size - 1 < index)
                        colorType1.add(colorList[random.nextInt(colorList.size)])
                }
            }

            val firstName =
                if (TextUtils.isEmpty(bean.name ?: "")) ""
                else (bean.name ?: "").substring(0, 1)


            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = when (type) {
                                0 -> colorResource(id = colorType0[index])
                                1 -> colorResource(id = colorType1[index])
                                else -> Color.Black
                            }
                        )
                        .padding(20.dp, 10.dp, 10.dp, 5.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = when (type) {
                                    0 -> colorResource(id = colorType0[index])
                                    1 -> colorResource(id = colorType1[index])
                                    else -> Color.Black
                                }
                            )
                            .border(
                                width = 3.dp,
                                color = Color.White,
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
                    Text(
                        text = bean.name ?: "",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            val childrens = bean.children ?: listOf()
            if (!childrens.isNullOrEmpty()) {
                var i = 0
                while (i < childrens.size) {
                    if (i + 1 <= childrens.size - 1) {
                        val id = childrens[i].id ?: ""
                        val name = childrens[i].name ?: ""
                        val id1 = childrens[i + 1].id ?: ""
                        val name1 = childrens[i + 1].name ?: ""
                        item { SquareChildren(actions = actions, id to name, id1 to name1) }
                        i += 2
                    } else {
                        val id = childrens[i].id ?: ""
                        val name = childrens[i].name ?: ""
                        item { SquareChildren(actions = actions, id to name) }
                        i += 1
                    }
                }
            }
            val articles = bean.articles ?: listOf()
            if (!bean.articles.isNullOrEmpty()) {
                var i = 0
                while (i < articles.size) {
                    if (i + 1 <= articles.size - 1) {
                        val a = articles[i]
                        val a1 = articles[i + 1]
                        item { SquareListArticles(actions = actions, a, a1) }
                        i += 2
                    } else {
                        val a = articles[i]
                        item { SquareListArticles(actions = actions, a) }
                        i += 1
                    }
                }
            }
        }
    }
}

@Composable
private fun SquareChildren(actions: MainActions, vararg list: Pair<String, String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ids = List<String>(list.size) { i -> list[i].first }
        val names = List<String>(list.size) { i -> list[i].second }
        if (list.size == 1) {
            childrenBox(names[0], Modifier.padding(20.dp, 0.dp)) {
                actions.jumpArticleList(ids[0], names[0])
            }
        } else {
            childrenBox(
                names[0],
                Modifier
                    .padding(20.dp, 0.dp)
                    .weight(1f)
            ) {
                actions.jumpArticleList(ids[0], names[0])
            }
            childrenBox(
                names[1],
                Modifier
                    .padding(20.dp, 0.dp)
                    .weight(1f)
            ) {
                actions.jumpArticleList(ids[1], names[1])
            }
        }
    }
}

@Composable
private fun SquareListArticles(actions: MainActions, vararg list: ArticleBean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (list.size == 1) {
            childrenBox(list[0].title ?: "", Modifier.padding(20.dp, 0.dp)) {
                actions.enterArticle(list[0])
            }
        } else {
            childrenBox(
                list[0].title ?: "",
                Modifier
                    .padding(20.dp, 0.dp)
                    .weight(1f)
            ) {
                actions.enterArticle(list[0])
            }
            childrenBox(
                list[1].title ?: "",
                Modifier
                    .padding(20.dp, 0.dp)
                    .weight(1f)
            ) {
                actions.enterArticle(list[1])
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
    if (colorList.isEmpty()) {
        for (i in 0..18) {
            val resId: Int = ResourceUtil.getResId("in" + (i + 1), R.color::class.java)
            colorList.add(resId)
        }
    }
}



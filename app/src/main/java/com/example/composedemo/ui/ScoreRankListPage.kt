package com.example.composedemo.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.RankBean
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.topBar
import com.example.composedemo.util.MMkvHelper
import com.example.composedemo.viewmodel.MyViewModel

private  var page = 1

@ExperimentalFoundationApi
@Composable
fun ScoreRankListPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {
    val scoreData by myViewModel.listScore.observeAsState()
    val list by remember { mutableStateOf(mutableListOf<RankBean>()) }

    if (scoreData != null) {
        if (scoreData?.first == false) list.clear()
        list.addAll(scoreData?.second ?: mutableListOf())
    }

    if (list.isEmpty()) {
        page = 1
        myViewModel.listScoreRank(false,page)
    }

    Column(modifier = modifier) {
        topBar(title = "积分排行榜", click = { actions.upPress() })
        ScoreRankList(Modifier.weight(1f), myViewModel,list)
        val userInfo = MMkvHelper.getInstance().userInfo
        ScoreRankItem(
            getRank(userInfo?.rank ?: ""),
            userInfo.username ?: "",
            userInfo.coinCount ?: ""
        )
    }
}

@Composable
private fun ScoreRankList(
    modifier: Modifier,
    myViewModel: MyViewModel,
    listScore: MutableList<RankBean>
) {
    var refreshingState by remember { mutableStateOf(false) }
    Column(modifier) {
        SwipeToRefreshAndLoadLayout(
            refreshingState = refreshingState,
            loadState = refreshingState,
            onRefresh = {
                page = 1
                myViewModel.listScoreRank(false,page)
                refreshingState = true
            },
            onLoad = {
                page += 1
                myViewModel.listScoreRank(true,page)
                refreshingState = true
            }
        ) {
            LazyColumn {
                items(listScore) { item ->
                    ScoreRankItem(
                        getRank(item.rank ?: ""),
                        item.username ?: "",
                        (item.coinCount ?: "").toString()
                    )
                }
            }
            refreshingState = false
         }
    }
}


@Composable
private fun ScoreRankItem(
    rankTriple: Triple<String, Boolean, Int>,
    name: String,
    count: String
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = colorResource(id = R.color.main_text))
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (rankTriple.second)
            Image(
                modifier = Modifier
                    .width(55.dp)
                    .padding(5.dp, 0.dp),
                painter = painterResource(id = rankTriple.third),
                contentDescription = ""
            )
        else
            Text(
                text = rankTriple.first,
                modifier = Modifier.width(55.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.Black
            )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = name ?: "",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = count ?: "",
            fontSize = 18.sp,
            color = colorResource(id = R.color.main_text)
        )
    }
}


private fun getRank(rank: String): Triple<String, Boolean, Int> {
    var isShowIm = false
    var im = 0
    when (rank) {
        "1" -> {
            isShowIm = true
            im = R.mipmap.gold_crown
        }
        "2" -> {
            isShowIm = true
            im = R.mipmap.silver_crown
        }
        "3" -> {
            isShowIm = true
            im = R.mipmap.cooper_crown
        }
    }
    return Triple(rank, isShowIm, im)
}
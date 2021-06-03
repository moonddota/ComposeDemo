package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.bean.BannerRes
import com.example.composedemo.bean.RankBean
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.constant.C
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.util.MMkvHelper
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun ScoreRankListPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {

    val coinCount: String = MMkvHelper.getInstance().userInfo?.coinCount ?: "0"

    Scaffold(
        modifier = modifier,
        topBar = {
            PlayAppBar(
                title = "我的积分", click = { actions.upPress() },
                showRight = true,
                rightImg = painterResource(id = R.mipmap.ic_question),
                rightClick = {
                    actions.enterArticle(
                        ArticleBean(
                            title = "本站积分规则",
                            link = C.INTERGRAL_URL
                        )
                    )
                }
            )
        },
        content = {
            Column {
                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = coinCount,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.main_text)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "我的积分",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    color = colorResource(id = R.color.black)
                )
                Spacer(modifier = Modifier.height(80.dp))
                ScoreRankListPageContent(myViewModel)
            }
        }
    )
}

@Composable
fun ScoreRankListPageContent(myViewModel: MyViewModel) {
    var loadArticleState by remember { mutableStateOf(false) }
    val data by myViewModel.listIntegralData.observeAsState(PlayLoading)

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.listIntegral(false)
    }

    SetLcePage(playState = data,
        onErrorClick = {
            myViewModel.listIntegral(false)
        }
    ) {
        val list = (data as PlaySuccess<MutableList<RankBean>>).data
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(15.dp)
            ) {
                Image(painter = painterResource(id = R.mipmap.ic_my_score), contentDescription = "")
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "积分记录", color = Color.Black, fontSize = 18.sp)
            }
        }

    }

}
package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.AppUtils
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.bean.BannerRes
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.viewmodel.MyViewModel
import com.zj.banner.BannerPager
import com.zj.banner.model.BaseBannerBean
import com.zj.banner.ui.indicator.NumberIndicator

@ExperimentalFoundationApi
@Composable
fun HomePage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {

    var loadArticleState by remember { mutableStateOf(false) }
    var refreshingState by remember { mutableStateOf(false) }
    val bannerData by myViewModel.banners.observeAsState(PlayLoading)
    val homeListData by myViewModel.homeListData.observeAsState()

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.getBanner()
        myViewModel.listArticle(false)
    }

    SetLcePage(playState = bannerData,
        onErrorClick = {
            myViewModel.getBanner()
            myViewModel.listArticle(false)
        }
    ) {
        val banners = arrayListOf<BannerBean>()
        (bannerData as PlaySuccess<List<BannerRes>>).data.forEach {
            banners.add(BannerBean(it.imagePath, it))
        }

        SwipeToRefreshAndLoadLayout(
            refreshingState = refreshingState,
            loadState = refreshingState,
            onRefresh = {
                myViewModel.listArticle(false)
                refreshingState = true
            },
            onLoad = {
                myViewModel.listArticle(true)
                refreshingState = true
            }
        ) {
            refreshingState = false
            HomeCount(modifier, actions, banners, homeListData ?: mutableListOf(), myViewModel)
        }
    }
}

@Composable
fun HomeCount(
    modifier: Modifier,
    actions: MainActions,
    banners: ArrayList<BannerBean>,
    homeListData: MutableList<ArticleBean>,
    myViewModel: MyViewModel
) {
    LazyColumn(modifier = modifier) {
        item {
            PlayAppBar(
                title = AppUtils.getAppName(),
                showBack = false,
                showRight = true,
                rightImg = painterResource(id = R.drawable.ic_search),
                rightClick = {
                    actions.jumpSearchPage()
                })
        }
        item {
            BannerPager(
                items = banners,
                indicator = NumberIndicator()
            ) {
                actions.enterArticle(
                    ArticleBean(
                        title = it.bannerRes?.title,
                        link = it.bannerRes?.url
                    )
                )
            }
        }
        itemsIndexed(homeListData) { index, item ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.Blue, RoundedCornerShape(4.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable { actions.enterArticle(item) },
            ) {
                var proportion = 3f
                if (item.envelopePic.isNullOrEmpty()) {
                    proportion = 1f
                } else {
                    LoadImage(
                        url = item.envelopePic ?: "",
                        contentDescription = "LoadImage",
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                            .height(100.dp)
                    )
                }
                homeList(
                    Modifier.weight(proportion),
                    item,
                    myViewModel
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}


data class BannerBean(
    override val data: Any? = null,
    val bannerRes: BannerRes? = null
) : BaseBannerBean()


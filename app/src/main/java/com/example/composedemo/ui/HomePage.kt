package com.example.composedemo.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.composedemo.common.topBar
import com.example.composedemo.common.SwipeToRefreshAndLoadLayout
import com.example.composedemo.common.lce.SetLcePage
import com.example.composedemo.model.PlayLoading
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.viewmodel.MyViewModel
import com.zj.banner.BannerPager
import com.zj.banner.model.BaseBannerBean
import com.zj.banner.ui.indicator.NumberIndicator

private val banners by lazy { arrayListOf<BannerBean>() }

@ExperimentalFoundationApi
@Composable
fun HomePage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {

    val bannerData by myViewModel.banners.observeAsState(PlayLoading)
    val homeListData by myViewModel.homeListData.observeAsState()
    val homeList by remember { mutableStateOf(mutableListOf<ArticleBean>()) }

    if (bannerData is PlaySuccess<*>) {
        (bannerData as PlaySuccess<List<BannerRes>>).data.forEach {
            banners.add(BannerBean(it.imagePath, it))
        }
    }

    if (homeListData != null) {
        if (homeListData?.first == false) homeList.clear()
        homeList.addAll(homeListData?.second ?: mutableListOf())
    }

    if (banners.isEmpty()) myViewModel.getBanner()

    SetLcePage(playState = bannerData,
        onErrorClick = { myViewModel.getBanner() }
    ) {
        HomeCount(modifier, actions, banners, homeList, myViewModel)
    }
}

@Composable
fun HomeCount(
    modifier: Modifier,
    actions: MainActions,
    banners: ArrayList<BannerBean>,
    homeList: MutableList<ArticleBean>,
    myViewModel: MyViewModel
) {
    var page by remember { mutableStateOf(0) }
    var refreshingState by remember { mutableStateOf(false) }

    if (homeList.isEmpty()) {
        page = 0
        myViewModel.listArticle(false, page)
    }

    SwipeToRefreshAndLoadLayout(
        refreshingState = refreshingState,
        loadState = refreshingState,
        onRefresh = {
            page = 0
            myViewModel.listArticle(false, page)
            refreshingState = true
        },
        onLoad = {
            page += 1
            myViewModel.listArticle(true, page)
            refreshingState = true
        }
    ) {
        refreshingState = false
        LazyColumn(modifier = modifier) {
            item {
                topBar(
                    title = AppUtils.getAppName(),
                    showBack = false,
                    showRight = true,
                    rightImg = painterResource(id = R.drawable.ic_search),
                    rightClick = { actions.jumpSearchPage() })
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
            if (homeList.isEmpty())
                item { NoContent() }
            else
                items(homeList) { item ->
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
}


data class BannerBean(
    override val data: Any? = null,
    val bannerRes: BannerRes? = null
) : BaseBannerBean()


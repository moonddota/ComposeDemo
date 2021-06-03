package com.example.composedemo.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier

import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.bean.BannerRes
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
    val homeList by myViewModel.homeListData.observeAsState()

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
        Column(modifier = modifier) {
            StatusBarHeight()
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
                ArticleListPaging(actions, homeList, myViewModel)
                Log.e("tag","${homeList?.size?:0}")
                refreshingState = false
            }
        }

    }
}

data class BannerBean(
    override val data: Any? = null,
    val bannerRes: BannerRes? = null
) : BaseBannerBean()


package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.common.topBar
import com.example.composedemo.viewmodel.MyViewModel


@ExperimentalFoundationApi
@Composable
fun OpenSourcePage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {

    Column(modifier = modifier) {
        topBar(
            title = stringResource(id = R.string.mine_open_source_project),
            click = { actions.upPress() })

        LazyColumn {
            itemsIndexed(items = OpenSourceProList) { index, item ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        actions.enterArticle(
                            ArticleBean(
                                title = item.author ?: "",
                                link = item.link ?: ""
                            )
                        )
                    }
                    .padding(16.dp, 8.dp)) {
                    Text(
                        text = item.author ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.main_text)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = item.content ?: "", color = Color.Black)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = colorResource(id = R.color.main_text))
                )
            }
        }
    }
}

private val OpenSourceProList by lazy {
    arrayListOf(
        OpenSourcePro(
            "Justson/AgentWeb",
            "AgentWeb is a powerful library based on Android WebView.",
            "https://github.com/Justson/AgentWeb"
        ),
        OpenSourcePro(
            "google/flexbox-layout",
            "Flexbox for Android",
            "https://github.com/google/flexbox-layout"
        ),
        OpenSourcePro(
            "alibaba/vlayout",
            "Project vlayout is a powerfull LayoutManager extension for RecyclerView, it provides a group of layouts for RecyclerView. Make it able to handle a complicate situation when grid, list and other layouts in the same recyclerview.",
            "https://github.com/alibaba/vlayout"
        ),
        OpenSourcePro(
            "square/okhttp",
            "Square’s meticulous HTTP client for Java and Kotlin.",
            "https://github.com/square/okhttp"
        ),
        OpenSourcePro(
            "square/retrofit",
            "A type-safe HTTP client for Android and the JVM",
            "https://github.com/square/retrofit"
        ),
        OpenSourcePro(
            "ReactiveX/RxJava",
            "RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.",
            "https://github.com/ReactiveX/RxJava"
        ),
        OpenSourcePro(
            "ReactiveX/RxAndroid",
            "RxJava bindings for Android",
            "https://github.com/ReactiveX/RxAndroid"
        ),
        OpenSourcePro(
            "greenrobot/EventBus",
            "Event bus for Android and Java that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality.",
            "https://github.com/greenrobot/EventBus"
        ),
        OpenSourcePro(
            "bumptech/glide",
            "An image loading and caching library for Android focused on smooth scrolling",
            "https://github.com/bumptech/glide"
        ),
        OpenSourcePro(
            "alibaba/ARouter",
            "A framework for assisting in the renovation of Android componentization (帮助 Android App 进行组件化改造的路由框架)",
            "https://github.com/alibaba/ARouter"
        ),
        OpenSourcePro(
            "gyf-dev/ImmersionBar",
            "android 4.4以上沉浸式状态栏和沉浸式导航栏管理，适配横竖屏切换、刘海屏、软键盘弹出等问题，可以修改状态栏字体颜色和导航栏图标颜色，以及不可修改字体颜色手机的适配，适用于Activity、Fragment、DialogFragment、Dialog，PopupWindow，一句代码轻松实现，以及对bar的其他设置，详见README。",
            "https://github.com/gyf-dev/ImmersionBar"
        ),
        OpenSourcePro(
            "JeasonWong/Particle",
            "手摸手教你用Canvas实现简单粒子动画",
            "https://github.com/JeasonWong/Particle"
        ),
        OpenSourcePro(
            "youth5201314/banner",
            "一款2.0版本Banner ！Android广告图片轮播控件，内部基于ViewPager2实现，Indicator和UI都可以自定义",
            "https://github.com/youth5201314/banner"
        ),
        OpenSourcePro(
            "KingJA/LoadSir",
            "A lightweight, good expandability Android library used for displaying different pages like loading, error, empty, timeout or even your custom page when you load a page.(优雅地处理加载中，重试，无数据等)",
            "https://github.com/KingJA/LoadSir"
        ),
        OpenSourcePro(
            "CymChad/BaseRecyclerViewAdapterHelper",
            "BRVAH:Powerful and flexible RecyclerAdapter",
            "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
        ),
        OpenSourcePro(
            "hackware1993/MagicIndicator",
            "A powerful, customizable and extensible ViewPager indicator framework. As the best alternative of ViewPagerIndicator, TabLayout and PagerSlidingTabStrip.",
            "https://github.com/hackware1993/MagicIndicator"
        ),
        OpenSourcePro(
            "Tencent/MMKV",
            "MMKV 是基于 mmap 内存映射的 key-value 组件，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强。从 2015 年中至今在微信上使用，其性能和稳定性经过了时间的验证。近期也已移植到 Android / macOS / Win32 / POSIX 平台，一并开源。",
            "https://github.com/Tencent/MMKV"
        ),
        OpenSourcePro(
            "scwang90/SmartRefreshLayout",
            "下拉刷新、上拉加载、二级刷新、淘宝二楼、RefreshLayout、OverScroll，Android智能下拉刷新框架，支持越界回弹、越界拖动，具有极强的扩展性，集成了几十种炫酷的Header和 Footer。",
            "https://github.com/scwang90/SmartRefreshLayout"
        ),
        OpenSourcePro(
            "HujiangTechnology/gradle_plugin_android_aspectjx",
            "A Android gradle plugin that effects AspectJ on Android project and can hook methods in Kotlin, aar and jar file.",
            "https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx"
        ),
        OpenSourcePro(
            "franmontiel/PersistentCookieJar",
            "A persistent CookieJar implementation for OkHttp 3 based on SharedPreferences.",
            "https://github.com/franmontiel/PersistentCookieJar"
        )
    )
}


data class OpenSourcePro(
    var author: String? = null,
    val content: String? = null,
    val link: String? = null
)
package com.example.composedemo.common.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.StringUtils
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.common.BookmarkButton
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.util.getHtmlText
import com.example.composedemo.util.toast
import com.example.composedemo.util.toastResource
import com.example.composedemo.viewmodel.MyViewModel

@Suppress("DEPRECATION") // allow ViewModelLifecycleScope call
@Composable
fun ArticlePage(
    modifier: Modifier,
    article: ArticleBean?,
    viewModel: MyViewModel,
    onBack: () -> Unit
) {
    val x5WebView = rememberX5WebViewWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            PlayAppBar(getHtmlText(article?.title ?: "文章详情"), click = {
                if (x5WebView.canGoBack()) {
                    //返回上个页面
                    x5WebView.goBack()
                } else {
                    onBack.invoke()
                }
            })
        },
        content = {
            // Adds view to Compose
            AndroidView(
                { x5WebView },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp),
            ) { x5WebView ->
                // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
                // is stored for later, Compose doesn't recognize state reads
                x5WebView.loadUrl(article?.link)
            }
        },
        bottomBar = {
            BottomBar(
                article = article,
                viewModel = viewModel
            )
        }
    )
}

@Composable
private fun BottomBar(
    article: ArticleBean?,
    viewModel: MyViewModel
) {
    var favoriteIcon by remember { mutableStateOf(if (article?.collect == false) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite) }
    BottomBar(article, viewModel, favoriteIcon)
    val collectsStatus by viewModel.collectsStatus.observeAsState()
    article?.collect = collectsStatus
    when (collectsStatus) {
        true -> {
            favoriteIcon = Icons.Filled.Favorite
            BottomBar(article, viewModel, favoriteIcon)
        }
        false -> {
            favoriteIcon = Icons.Filled.FavoriteBorder
            BottomBar(article, viewModel, favoriteIcon)
        }
        null -> {
        }
    }
}

@Composable
fun BottomBar(
    article: ArticleBean?,
    viewModel: MyViewModel,
    favoriteIcon: ImageVector
) {
    var loadState by remember { mutableStateOf(false) }
    Surface(elevation = 2.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                if (favoriteIcon == Icons.Filled.Favorite) {
                    viewModel.cancelCollects(article?.id ?: "")
                } else {
                    viewModel.toCollects(article?.id ?: "")
                }
            }) {
                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = "收藏"
                )
            }
            BookmarkButton(
                isBookmarked = loadState,
                onClick = {
                    toast(if (loadState) "取消书签(假的)" else "添加书签(假的)")
                    loadState = !loadState
                }
            )
            val context = LocalContext.current
            IconButton(onClick = {
                sharePost(
                    article?.title ?: "",
                    article?.link ?: "",
                    context
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "分享"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { toastResource(R.string.feature_not_available) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_text_settings),
                    contentDescription = "字体设置"
                )
            }
        }
    }
}

private fun sharePost(title: String, post: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, post)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            StringUtils.getString(R.string.share_article)
        )
    )
}
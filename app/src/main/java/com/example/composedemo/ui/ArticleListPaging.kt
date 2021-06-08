package com.example.composedemo.ui

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.viewmodel.MyViewModel
import java.lang.String


@ExperimentalFoundationApi
@Composable
fun ArticleListPaging(
    actions: MainActions,
    list: MutableList<ArticleBean>?,
    myViewModel: MyViewModel
) {
    Log.e("size", " size     ${list?.size}")
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(5.dp),
    ) {
        itemsIndexed(list?: mutableListOf()){index, item ->
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


@Composable
fun homeList(
    modifier: Modifier,
    item: ArticleBean,
    myViewModel: MyViewModel
) {
    Column(modifier = modifier) {
        Row {
            Title( //文字部分
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                item
            )
            Text(
                text = item.niceDate ?: "",
                fontSize = 14.sp,
                color = colorResource(id = R.color.FF222222),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Text(
            text = item.title ?: "",
            fontSize = 14.sp,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(5.dp)
        )
        Row(modifier = Modifier.padding(5.dp)) {
            Text(
                text = "作者：${String.format("%s·%s", item.superChapterName, item.chapterName)}",
                fontSize = 14.sp,
                color = colorResource(id = R.color.FF222222),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically),
            )
            FollowBtn( //按钮
                Modifier.align(Alignment.CenterVertically),
                item,
                myViewModel
            )
        }

    }
}

@Composable
fun Title(modifier: Modifier, item: ArticleBean) {
    Row(modifier = modifier) {
        if (item.fresh == true)
            Text(
                text = "新",
                fontSize = 16.sp,
                color = colorResource(id = R.color.main_text),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(5.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            colorResource(id = R.color.main_text)
                        )
                    )
                    .padding(5.dp)
            )
        Text(
            text = if ((item.author).isNullOrEmpty()) item.shareUser
                ?: "" else item.author ?: "",
            fontSize = 16.sp,
            color = colorResource(id = R.color.FF222222),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(5.dp, 0.dp)
        )
    }
}

@Composable
fun FollowBtn(
    modifier: Modifier,
    item: ArticleBean,
    myViewModel: MyViewModel
) {
    var favoriteIcon by remember { mutableStateOf(if (item.collect == false) Icons.Filled.FavoriteBorder else Icons.Filled.Favorite) }
    IconButton(modifier = modifier, onClick = {
        if (favoriteIcon == Icons.Filled.Favorite)
            myViewModel.launch(null, {}, {
                myViewModel.repository.cancelCollects(item.id ?: "")
            }, {
                favoriteIcon = Icons.Filled.FavoriteBorder
            })
        else
            myViewModel.launch(null, {}, {
                myViewModel.repository.toCollects(item.id ?: "")
            }, {
                favoriteIcon = Icons.Filled.Favorite
            })
    }) {
        Icon(
            imageVector = favoriteIcon,
            contentDescription = "收藏"
        )
    }
}







package com.example.composedemo.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.UserInfo
import com.example.composedemo.util.toast
import com.example.composedemo.viewmodel.MyViewModel
import java.lang.String

@ExperimentalFoundationApi
@Composable
fun MainPage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {
    var loadArticleState by remember { mutableStateOf(false) }
    val userInfo by myViewModel.userInfo.observeAsState()

    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.getIntegral()
    }

    Column() {
        MainPageTop(actions, modifier, userInfo)
        MainPageContents(actions, userInfo)
    }
}

@Composable
fun MainPageTop(actions: MainActions, modifier: Modifier, userInfo: UserInfo?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.main_text))
    ) {
        StatusBarHeight()
        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = { actions.jumpSetting() }) {
            Icon(
                painter = painterResource(R.mipmap.ic_mine_set),
                contentDescription = "",
                tint = Color.White
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 20.dp, 0.dp, 20.dp)
            .clickable {
                if (userInfo == null) {
                    actions.jumpLogin()
                }
            }) {
            Image(
                painter = painterResource(id = R.mipmap.ic_mine_head),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp, 60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userInfo?.username ?: "",
                    color = Color.White,
                    fontSize = 20.sp
                )
                Row {
                    Text(
                        text = String.format("ID: %s", userInfo?.userId ?: ""),
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = String.format("lv.%d", userInfo?.level ?: 0),
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp, 0.dp)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .background(
                        color = colorResource(id = R.color.main_text_light),
                        shape = RoundedCornerShape(10.dp, 0.dp, 0.dp, 10.dp)
                    )
                    .clickable {
                        toast("排行榜")
                    }) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.mipmap.ic_mine_score),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "积分排行榜", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun MainPageContents(actions: MainActions, userInfo: UserInfo?) {
    MainPageCountent(
        R.mipmap.ic_mine1,
        "我的积分",
        String.format("当前积分" + ": %s", userInfo?.coinCount ?: "")
    ) {
        toast("我的积分")
    }
    MainPageCountent(
        R.mipmap.ic_mine2,
        "我的收藏",
        ""
    ) {
        toast("我的收藏")
    }
    MainPageCountent(
        R.mipmap.ic_mine3,
        "我的分享",
        ""
    ) {
        toast("我的分享")
    }
    MainPageCountent(
        R.mipmap.ic_mine4,
        "开源项目",
        ""
    ) {
        toast("开源项目")
    }
    MainPageCountent(
        R.mipmap.ic_mine5,
        "关于作者",
        ""
    ) {
        toast("关于作者")
    }
}

@Composable
fun MainPageCountent(im: Int?, title: kotlin.String, countent: kotlin.String, onClick: () -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp)
        ) {
            if (im != null) {
                Image(painter = painterResource(id = im), contentDescription = "")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = title, color = Color.Black, modifier = Modifier.weight(1f))
            Text(text = countent, color = Color.Black)
            IconButton(onClick = { onClick() }) {
                Icon(painter = painterResource(id = R.mipmap.ic_mine_into), contentDescription = "")
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.main_text))
        )
    }
}

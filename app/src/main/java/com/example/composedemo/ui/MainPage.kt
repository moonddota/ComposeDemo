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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.UserInfo
import com.example.composedemo.util.MMkvHelper
import com.example.composedemo.util.toast
import com.example.composedemo.viewmodel.MyViewModel
import java.lang.String

private var userInfoData :UserInfo ?= null

@ExperimentalFoundationApi
@Composable
fun MainPage(actions: MainActions, modifier: Modifier, myViewModel: MyViewModel) {
     val userInfo by myViewModel.userInfo.observeAsState()

      userInfoData = userInfo
      if (userInfoData == null){
          myViewModel.getIntegral()
      }

    Column() {
        MainPageTop(actions, myViewModel, modifier, userInfoData)
        MainPageContents(actions, userInfo)
    }
}

@Composable
fun MainPageTop(
    actions: MainActions,
    myViewModel: MyViewModel,
    modifier: Modifier,
    userInfo: UserInfo?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.main_text))
    ) {
        StatusBarHeight()
        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = { checkLogin { actions.jumpSetting() } }) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = String.format("ID: %s", userInfo?.userId ?: ""),
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
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
                    .clickable { actions.jumpScoreRankListPage() })
            {
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
        stringResource(id = R.string.mine_integral),
        String.format("当前积分" + ": %s", userInfo?.coinCount ?: "")
    ) {
        checkLogin { actions.jumpMyScorePage() }
    }
    MainPageCountent(R.mipmap.ic_mine2, stringResource(id = R.string.mine_collect), "") {
        checkLogin { actions.jumpMyCollectPage() }
    }
    MainPageCountent(
        R.mipmap.ic_mine3, "我的分享", ""
    ) {
        toast("我的分享")
    }
    MainPageCountent(
        R.mipmap.ic_mine4, stringResource(id = R.string.mine_open_source_project), ""
    ) {
        actions.jumpOpenSourcePage()
    }
    MainPageCountent(R.mipmap.ic_mine5, "关于作者", "") {
        toast("关于作者")
    }
    MainPageCountent(R.mipmap.ic_mine5, "draw绘画", "") {
        actions.jumpDrawPageL()
    }
}

@Composable
fun MainPageCountent(im: Int?, title: kotlin.String, countent: kotlin.String, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
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
            Icon(painter = painterResource(id = R.mipmap.ic_mine_into), contentDescription = "")
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.main_text))
        )
    }
}


private fun checkLogin(function: () -> Unit) {
    val userInfo = MMkvHelper.getInstance().userInfo
    if (userInfo == null) {
        toast("清先登录")
    } else {
        function()
    }
}

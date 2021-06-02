package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.AppUtils
import com.example.composedemo.R
import com.example.composedemo.common.PlayAppBar
import com.example.composedemo.util.getHtmlText
import com.example.composedemo.util.toast
import com.example.composedemo.viewmodel.MyViewModel


@ExperimentalFoundationApi
@Composable
fun SettingPage(actions: MainActions, myViewModel: MyViewModel) {

    var loadArticleState by remember { mutableStateOf(false) }
    var showClierDialog by remember { mutableStateOf(false) }
    var showVersionDialog by remember { mutableStateOf(false) }
    val cacheData by myViewModel.cacheData.observeAsState()
    val isLogout by myViewModel.isLogout.observeAsState()
    if (!loadArticleState) {
        loadArticleState = true
        myViewModel.getCache()
    }

    when (isLogout) {
        true -> {
            actions.upPress()
            actions.jumpLogin()
        }
        else -> {
            Scaffold(
                topBar = {
                    PlayAppBar(getHtmlText("设置"), click = {
                        actions.upPress()
                    })
                },
                content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        MainPageCountent(null, "语言设置", "zh-CN") {
                            toast("待开发")
                        }
                        MainPageCountent(null, "清除缓存", cacheData ?: "") {
                            showClierDialog = true
                        }
                        MainPageCountent(null, "当前版本", AppUtils.getAppVersionName()) {
                            showVersionDialog = true
                        }
                        MainPageCountent(null, "版本声明", "") {
                            toast("版本声明")
                        }
                        MainPageCountent(null, "关于我们", "") {
                            toast("关于我们")
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(50.dp, 0.dp),
                            shape = RoundedCornerShape(30.dp),
                            onClick = {
                                myViewModel.logout()
                            }) {
                            Text(
                                text = "退出登录",
                                color = Color.White,
                                style = MaterialTheme.typography.h5
                            )
                        }
                    }
                    if (showClierDialog)
                        clearDialog(myViewModel) {
                            showClierDialog = false
                        }

                    if (showVersionDialog)
                        versionDialog {
                            showVersionDialog = false
                        }
                }
            )
        }
    }
}

@Composable
fun clearDialog(viewModel: MyViewModel, onDismiss: () -> Unit = {}) {
    AlertDialog(
        onDismissRequest = {},
        text = {
            Text(
                text = "确定要清除缓存吗？",
                color = Color.Black,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        buttons = {
            Row(modifier = Modifier.fillMaxWidth()) {
                dialogButton(
                    Modifier.weight(1f),
                    "取消",
                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text_light))
                ) { onDismiss() }
                dialogButton(
                    Modifier.weight(1f),
                    "确定",
                    ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text))
                ) {
                    onDismiss()
                    viewModel.clearCache()
                }
            }
        }
    )
}

@Composable
fun versionDialog(onDismiss: () -> Unit = {}) {
    AlertDialog(
        onDismissRequest = {},
        text = {
            Text(
                text = "当前版本 ：V${AppUtils.getAppVersionName()}   是最新版本",
                color = Color.Black,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        buttons = {
            dialogButton(
                Modifier.fillMaxWidth(),
                "确定",
                ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_text))
            ) { onDismiss() }
        }
    )
}


@Composable
fun dialogButton(modifier: Modifier, title: String, color: ButtonColors, onClick: () -> Unit = {}) {
    Button(
        modifier = modifier,
        colors = color,
        onClick = { onClick() }) {
        Text(text = title, color = Color.White)
    }
}
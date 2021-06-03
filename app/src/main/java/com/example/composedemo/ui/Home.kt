package com.example.composedemo.ui

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.BarUtils
import com.example.composedemo.App
import com.example.composedemo.R
import com.example.composedemo.viewmodel.MyViewModel
import java.util.*

@ExperimentalFoundationApi
@Composable
fun Home(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val tabs = CourseTabs.values()
    val position by myViewModel.position.observeAsState()
    val baseTitle = stringResource(id = R.string.app_name)
    val (title, setTitle) = remember { mutableStateOf(baseTitle) }
    val (canPop, setCanPop) = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { controller, _, _ ->
        setCanPop(controller.previousBackStackEntry != null)
    }

    val toolBarIcon = remember { mutableStateOf(Icons.Default.Search) }

    Scaffold(
        modifier = modifier,
        backgroundColor = colorResource(id = R.color.white),
        bottomBar = {
            BottomNavigation(modifier = Modifier.height(56.dp)) {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        modifier = Modifier.background(color = colorResource(id = R.color.main_text)),
                        icon = {
                            Icon(
                                painter = painterResource(tab.icon),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                                    .background(colorResource(id = R.color.main_text))
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(tab.title).toUpperCase(Locale.ROOT),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        },
                        selected = tab == position,
                        onClick = { myViewModel.onPositionChanged(tab) },
                        alwaysShowLabel = false,
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (position) {
            CourseTabs.HOME_PAGE -> HomePage(actions, modifier, myViewModel)
            CourseTabs.PROJECT -> ProjectPage(actions, modifier, myViewModel)
            CourseTabs.OFFICIAL_ACCOUNT -> MessageList(getList(), Color.Red)
            CourseTabs.MINE -> MainPage(actions, modifier, myViewModel)
        }
    }
}

@Composable
fun MessageList(messages: List<String>, color: Color) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color)
    ) {
        items(count = 1) {
            messages.forEachIndexed { index, s ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(1.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(width = 1.dp, color = Color.Yellow)
                ) {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(1.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        onClick = {
                            Toast.makeText(App.instance, "$index  的内容是  $s", Toast.LENGTH_SHORT)
                                .show()
                        }) {
                        Text(
                            text = s,
                            modifier = Modifier.fillMaxHeight(),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
            }
        }

    }
}

fun getList(): List<String> {
    return listOf<String>(
        "aa",
        "bb",
        "cc",
        "dd",
        "ee",
        "ff",
        "gg",
        "hh",
        "qq",
        "11",
        "22",
        "33",
        "44",
        "55",
        "66",
        "77",
        "88",
        "99",
        "10",
        "100",
        "111"
    )
}


enum class CourseTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    HOME_PAGE(R.string.home_page, R.drawable.ic_nav_news_normal),
    PROJECT(R.string.project, R.drawable.ic_nav_tweet_normal),
    OFFICIAL_ACCOUNT(R.string.official_account, R.drawable.ic_nav_discover_normal),
    MINE(R.string.mine, R.drawable.ic_nav_my_normal)
}
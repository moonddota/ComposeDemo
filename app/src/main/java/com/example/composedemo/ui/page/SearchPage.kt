package com.example.composedemo.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R
import com.example.composedemo.bean.SearchHotkeyBean
import com.example.composedemo.common.signin.TextFieldState
import com.example.composedemo.ui.MainActions
import com.example.composedemo.ui.NoContent
import com.example.composedemo.ui.widget.StatusBarHeight
import com.example.composedemo.viewmodel.MyViewModel

@ExperimentalFoundationApi
@Composable
fun SearchPage(modifier: Modifier, actions: MainActions, myViewModel: MyViewModel) {

    val searchState = remember { TextFieldState() }
    var clickSearchState by remember { mutableStateOf(false) }
    val searchList by myViewModel.searchList.observeAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SearchPageTop(actions, searchState) {
                clickSearchState = true
                myViewModel.searchArticle(0,searchState.text)
            }
        },
        content = {
            if (searchState.text.isBlank() || !clickSearchState) {
                NoContent()
            } else {
                ArticleListPaging(actions,searchList?: mutableListOf(),myViewModel)
            }
        }
    )
}

@Composable
private fun SearchPageTop(actions: MainActions, searchState: TextFieldState, onSearch: () -> Unit) {
    Column {
        StatusBarHeight()
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            TopAppBar(
                backgroundColor = colorResource(id = R.color.main_text),
                elevation = 0.dp,
                modifier = Modifier.height(48.dp)
            ) {
                IconButton(
                    modifier = Modifier.wrapContentWidth(Alignment.Start),
                    onClick = { actions.upPress() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "back",
                        tint = Color.White
                    )
                }
                BasicTextField(
                    value = searchState.text,
                    onValueChange = { searchState.text = it },
                    textStyle = MaterialTheme.typography.subtitle1.copy(color = Color.White),
                    maxLines = 1,
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { onSearch() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
            if (searchState.text.isEmpty()) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    SearchHint()
                }
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(imageVector = Icons.Outlined.Search, contentDescription = "", tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text(text = "请输入关键字", color = Color.White)
    }
}

@Composable
private fun SearchList(actions: MainActions, list: MutableList<SearchHotkeyBean>) {

    LazyColumn {
        items(list) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(width = 1.dp, color = Color.Black)
                    .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${item.name}",
                    fontSize = 22.sp,
                    color = colorResource(id = R.color.main_text)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "类别：${item.category}")
            }
        }
    }
}
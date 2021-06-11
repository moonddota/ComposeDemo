package com.example.composedemo.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.composedemo.R
import com.example.composedemo.ui.StatusBarHeight

@Composable
fun topBar(
    title: String,
    showBack: Boolean = true,
    click: (() -> Unit)? = null,
    showRight: Boolean = false,
    rightImg: Painter = painterResource(id = R.mipmap.ic_question) ,
    rightClick: (() -> Unit)? = null,
) {
    Column(modifier = Modifier.background(color = colorResource(id = R.color.main_text))) {
        StatusBarHeight()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(43.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (showBack) {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start), onClick = click!!
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "back",
                        tint = Color.White
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start), onClick = {}
                ) {}
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                text = title,
                style = MaterialTheme.typography.subtitle1,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (showRight) {
                if (rightClick != null) {
                    IconButton(
                        modifier = Modifier.wrapContentWidth(Alignment.End),
                        onClick = rightClick
                    ) {
                        Icon(
                            painter = rightImg,
                            contentDescription = "more",
                            tint = Color.White
                        )
                    }
                }
            } else {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start), onClick = {}
                ) {}
            }
        }
    }
}
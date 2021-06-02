package com.example.composedemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blankj.utilcode.util.BarUtils
import com.example.composedemo.R
import com.sczhizhou.navpad.util.px2dp

@Composable
fun TopBar(title: String, actions: MainActions) {
    Column() {
        StatusBarHeight()
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.main_text))
        ) {
            IconButton(onClick = {
                actions.upPress
            }) {
                Icon(painter = painterResource(R.mipmap.ic_back), contentDescription = "")
            }
            Text(
                modifier = Modifier.padding(5.dp),
                text = title,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                letterSpacing = 5.sp,
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
fun StatusBarHeight() {
    Spacer(modifier = Modifier.height(px2dp(BarUtils.getStatusBarHeight().toFloat()).dp))
}
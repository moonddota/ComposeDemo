package com.example.composedemo.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.R

@Composable
fun MySelectTab(
    list: List<String>,
    modifier: Modifier = Modifier,
    position: Int,
    onClick: (index: Int) -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        list.forEachIndexed { index, s ->
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .border(width = 1.dp, color = Color.White)
                    .background(color = if (index == position) Color.White else colorResource(id = R.color.main_text))
                    .clickable { if (index != position) onClick(index) }
                    .padding(15.dp, 2.dp)
            ) {
                Text(
                    text = s,
                    fontSize = 18.sp,
                    color = if (index == position) colorResource(id = R.color.main_text) else Color.White
                )
            }
        }
    }


}
package com.example.composedemo.bean

data class RankListRes(
    var curPage: Int? = null,

    val offset: Int? = null,

    val over: Boolean? = null,

    val pageCount: Int? = null,

    val size: Int? = null,

    val total: Int? = null,

    val datas: List<RankBean>? = null
) {
}
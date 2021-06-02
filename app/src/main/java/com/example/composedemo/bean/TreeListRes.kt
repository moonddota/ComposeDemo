package com.example.composedemo.bean

data class TreeListRes(

    /*共用*/
    var name: String? = null,
    /*体系*/
    val courseId: String? = null,

    val id: String? = null,

    val order: Int? = null,

    val parentChapterId: Int? = null,

    val userControlSetTop: Boolean? = null,

    val visible: Int? = null,

    val children: List<ChildrenBean>? = null,

    /*导航*/
    val cid: String? = null,
    val articles: List<ArticleBean>? = null

)

data class ChildrenBean(
    private var courseId: String? = null,
    val id: String? = null,
    val name: String? = null,
    val order: Int? = null,
    val parentChapterId: Int? = null,
    val userControlSetTop: Boolean? = null,
    val visible: Int? = null,
    val children: List<*>? = null
)
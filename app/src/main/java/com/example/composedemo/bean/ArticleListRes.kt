package com.example.composedemo.bean


data class ArticleListRes(

    var curPage: Int? = null,

    var offset: Int? = null,

    var over: Boolean? = null,

    var pageCount: Int? = null,

    var size: Int? = null,

    var total: Int? = null,

    var datas: MutableList<ArticleBean>? = null

)


data class ArticleBean(
    var apkLink: String? = null,
    var audit: Int? = null,
    var author: String? = null,
    var canEdit: Boolean? = null,
    var chapterId: String? = null,
    var chapterName: String? = null,
    var collect: Boolean? = null,
    var courseId: String? = null,
    var desc: String? = null,
    var descMd: String? = null,
    var envelopePic: String? = null,
    var fresh: Boolean? = null,
    var id: String? = null,
    var link: String? = null,
    var niceDate: String? = null,
    var niceShareDate: String? = null,
    var origin: String? = null,
    var prefix: String? = null,
    var projectLink: String? = null,
    var publishTime: Long? = null,
    var realSuperChapterId: Int? = null,
    var selfVisible: Int? = null,
    var shareDate: Any? = null,
    var shareUser: String? = null,
    var superChapterId: Int? = null,
    var superChapterName: String? = null,
    var title: String? = null,
    var type: Int? = null,
    var userId: Int? = null,
    var visible: Int? = null,
    var zan: Int? = null,
    var tags: List<*>? = null
)
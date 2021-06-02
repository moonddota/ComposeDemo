package com.example.composedemo.bean

data class PublicAuthorListRes(
    var courseId: String? = null,

    val id: String? = null,

    val name: String? = null,

    val order: Int? = null,

    val parentChapterId: String? = null,

    val userControlSetTop: Boolean? = null,

    val visible: Int? = null,

    val children: List<*>? = null
) {
}
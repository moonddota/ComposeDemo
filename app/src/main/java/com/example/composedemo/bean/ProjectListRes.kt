package com.example.composedemo.bean


class ProjectListRes(
     var courseId: String? = null,
     val id: String? = null,
     val name: String? = null,
     val order: Int = 0,
     val parentChapterId: String? = null,
     val userControlSetTop: Boolean? = false,
     val visible: Int = 0,
     val children: List<*>? = null
)
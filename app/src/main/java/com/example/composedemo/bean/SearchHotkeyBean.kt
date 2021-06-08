package com.example.composedemo.bean

data class SearchHotkeyBean(
    //类别
    var category: String? = null,
    var icon: String? = null,
    var id: Int? = null,
    var link: String? = null,
    var name: String? = null,
    var order: Int? = null,
    var visible: Int? = null
)
package com.example.composedemo.util

import com.blankj.utilcode.util.ToastUtils
import com.example.composedemo.App

fun toast( msg: String) {
    ToastUtils.showShort(msg)
}

fun toastResource(Resource : Int){
    ToastUtils.showShort(App.instance.getString(Resource))

}

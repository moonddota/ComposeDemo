package com.example.composedemo

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

class App :Application(){

    companion object{
        @JvmStatic
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)
        //初始化mmkv
        MMKV.initialize(this)
    }

}
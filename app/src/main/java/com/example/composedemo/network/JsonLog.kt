package com.example.composedemo.network

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JsonLog {
    val LINE_SEPARATOR = System.getProperty("line.separator")
    fun printLine(tag: String, isTop: Boolean) {
        if (isTop) {
            Log.d(
                tag,
                "╔═══════════════════════════════════════════════════════════════════════════════════════"
            ); } else {
            Log.d(
                tag,
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            ); }
    }

    fun printJson(tag: String, msg: String, headString: String) {
        var message = ""
        try {
            if (msg.startsWith("{")) {
                val jsonObject = JSONObject(msg)

                message =
                    jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                val jsonArray = JSONArray(msg)
                message = jsonArray.toString(4)
            } else {
                message = msg
            }
        } catch (e : JSONException) {
            message = msg
        }
        printLine (tag, true)
        message = headString + LINE_SEPARATOR + message
        val lines = message.split (LINE_SEPARATOR!!)
        lines.forEach {
            Log.d(tag, "║ " + it)
        }
        printLine (tag, false)
    }


}
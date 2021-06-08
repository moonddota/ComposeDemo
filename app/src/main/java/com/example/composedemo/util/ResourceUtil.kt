package com.example.composedemo.util

object ResourceUtil {
    fun getResId(variableName: String?, c: Class<*>): Int {
        return try {
            val idField = c.getDeclaredField(variableName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
}
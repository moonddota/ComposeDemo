package com.example.composedemo.network
import android.os.Parcel

class BaseData<T> {
    var errorCode = 0
    var errorMsg: String = ""
    var data: T? = null
}


inline fun <reified T> Parcel.readMutableList(): MutableList<T> {
    @Suppress("UNCHECKED_CAST")
    return readArrayList(T::class.java.classLoader) as MutableList<T>
}
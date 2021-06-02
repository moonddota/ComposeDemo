package com.example.composedemo.network

interface ResponseFormatListener<T> {

    fun onSuccess(t: T?)

    fun onFailed(code: Int, msg: String)
}
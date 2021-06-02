package com.example.composedemo.network


import okhttp3.Interceptor
import okhttp3.Response

class RequestHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val addHeader = original.header("AddHeader") ?: "1"
        if (addHeader == "1") {
//            val userToken = SharedPrefUtils.instance.get(USER_TOKEN, "")
            val request = original.newBuilder()
//                .addHeader("Authorization", userToken)
                .method(original.method, original.body)
                .build()
            return chain.proceed(request)
        }
        return chain.proceed(original)
    }
}
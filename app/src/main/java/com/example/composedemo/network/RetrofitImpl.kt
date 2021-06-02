package com.example.composedemo.network

import com.example.composedemo.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitImpl private constructor() {

    private var gsons: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
    private var factory: GsonConverterFactory = GsonConverterFactory.create(gsons)
    private var mRetrofit: Retrofit? = null

    companion object {
        val instance = RetrofitHelperHolder.INSTANCE
    }

    private object RetrofitHelperHolder {
        val INSTANCE = RetrofitImpl()
    }


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun resetApp() {

        mRetrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BANSE_URL)
            .client(OkHttpClientImpl.generate())
            .addConverterFactory(factory)
            .build()
    }

    fun getRetrofit(): Retrofit {
        if (mRetrofit != null) {
            return mRetrofit as Retrofit
        } else {
            throw NullPointerException("u should init first")
        }
    }

    init {
        resetApp()
    }


}

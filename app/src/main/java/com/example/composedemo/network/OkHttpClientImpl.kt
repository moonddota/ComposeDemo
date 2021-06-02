package com.example.composedemo.network

import com.example.composedemo.util.AppProvider
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkHttpClientImpl {

    private var cookieJar: ClearableCookieJar? = null
    private fun getCookieJar(): ClearableCookieJar {
        if (cookieJar == null) {
            cookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(AppProvider.getInstance().app)
            )
        }
        return cookieJar!!
    }

    private var okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(HttpLogInterceptor())
        .addInterceptor(RequestHeaderInterceptor())
        .addInterceptor(ResponseErrorInterceptor())
        .cookieJar(getCookieJar())
        .retryOnConnectionFailure(true)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)

    private fun getOkHttpClient(): OkHttpClient {
        return okHttpClientBuilder.build()
    }

    companion object {
        fun generate(): OkHttpClient {
            return with(OkHttpClientImpl()) {
                getOkHttpClient()
            }
        }
    }

}

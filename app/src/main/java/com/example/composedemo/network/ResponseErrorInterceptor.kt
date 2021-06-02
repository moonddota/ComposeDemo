package com.example.composedemo.network

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http.promisesBody
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

class ResponseErrorInterceptor : Interceptor {
    val utf8: Charset = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val responseBody = response.body
        val contentLength = responseBody?.contentLength()
        if (!response.promisesBody()) {
            //END HTTP
        } else if (bodyEncoded(response.headers)) {
            //HTTP (encoded body omitted)
        } else {
            val source = responseBody?.source()
            source?.request(Long.MAX_VALUE)// Buffer the entire body.
            val buffer = source?.buffer()

            var charset = utf8
            val contentType = responseBody?.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(utf8) ?: utf8
                } catch (e: UnsupportedCharsetException) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response
                }
            }

            if (!isPlaintext(buffer)) {
                LogUtils.i(
                    "ResponseErrorInterceptor",
                    "<-- END HTTP (binary " + buffer?.size + "-byte body omitted)"
                )
                return response
            }

            if (contentLength != 0L) {
                val result = buffer?.clone()?.readString(charset)
//                LogUtils.e("ResponseErrorInterceptor", "result: $result")
                result?.let {
                    try {
                        val body =
                            Gson().fromJson<BaseData<Any>>(result, genericType<BaseData<Any>>())
                        return when (body.errorCode) {
                            0 -> response
                            else -> {
                                body.data = null
                                val newBody = ResponseBody.create(contentType, Gson().toJson(body))
                                response.newBuilder().body(newBody).build()
                            }
                        }
                    } catch (e: Exception) {

                    }
                }
            }

            LogUtils.i(
                "ResponseErrorInterceptor",
                "<-- END HTTP (" + buffer?.size + "-byte body)"
            )

        }
        return response
    }

    @Throws(EOFException::class)
    fun isPlaintext(buffer: Buffer?): Boolean {
        if (buffer == null) return false
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun bodyEncoded(headers: Headers?): Boolean {
        val contentEncoding: String? = headers?.get("Content-Encoding")
        return (contentEncoding != null) and (!(contentEncoding?.equals("identity", true) ?: false))

    }
}
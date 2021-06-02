package com.example.composedemo.network

/**
 * Format response
 * @param base Base implement
 * @return "T" data class
 */
fun <T>  formatResponse(base: BaseData<T>, listener: ResponseFormatListener<T>? = null): T? {
    return when (base.errorCode) {
        0 -> {
            listener?.onSuccess(base.data)
            base.data
        }
        else -> {
            listener?.onFailed(base.errorCode, base.errorMsg)
            null
        }
    }
}
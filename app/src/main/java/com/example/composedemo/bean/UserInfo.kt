package com.example.composedemo.bean

import android.os.Parcel
import android.os.Parcelable
import com.example.composedemo.util.parcelableCreatorOf
import com.example.composedemo.util.readMutableList


data class UserInfo(
    var email: String? = null,
    var icon: String? = null,
    var id: String? = null,
    var password: String? = null,
    var token: String? = null,
    var type: String? = null,
    var username: String? = null,
    var chapterTops: List<Int>? = null,
    var collectIds: List<Int>? = null,//收藏的文章id

    /**********************************************我的积分 */
    var coinCount: String? = null,
    var level: Int? = null,
    var rank: String? = null,
    var userId: String? = null,
    var reason: String? = null,
    var desc: String? = null,
    var date: String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readMutableList(),
        parcel.readMutableList(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(icon)
        parcel.writeString(id)
        parcel.writeString(password)
        parcel.writeString(token)
        parcel.writeString(type)
        parcel.writeString(username)
        parcel.writeList(chapterTops)
        parcel.writeList(collectIds)
        parcel.writeString(coinCount)
        parcel.writeValue(level)
        parcel.writeString(rank)
        parcel.writeString(userId)
        parcel.writeString(reason)
        parcel.writeString(desc)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreatorOf<UserInfo>()
    }


}
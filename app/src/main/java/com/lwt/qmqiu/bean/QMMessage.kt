package com.lwt.qmqiu.bean

import android.os.Parcel
import android.os.Parcelable

data class QMMessage(
        var from: String = "",
        var to: String = "",
        //0 普通消息 2 礼物 3 语音 4 图片
        var type: Int = 0,
        var message: String = "",
        var colorIndex: Int = 0,
        var imgPath: String = "",
        var currentCount: Int = 0,
        var time: Long = System.currentTimeMillis()) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(from)
        writeString(to)
        writeInt(type)
        writeString(message)
        writeInt(colorIndex)
        writeString(imgPath)
        writeInt(currentCount)
        writeLong(time)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QMMessage> = object : Parcelable.Creator<QMMessage> {
            override fun createFromParcel(source: Parcel): QMMessage = QMMessage(source)
            override fun newArray(size: Int): Array<QMMessage?> = arrayOfNulls(size)
        }
    }
}
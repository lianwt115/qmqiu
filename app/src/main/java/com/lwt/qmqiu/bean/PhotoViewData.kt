package com.lwt.qmqiu.bean

import android.os.Parcel
import android.os.Parcelable

data class PhotoViewData(var position: Int, var content: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(position)
        writeString(content)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PhotoViewData> = object : Parcelable.Creator<PhotoViewData> {
            override fun createFromParcel(source: Parcel): PhotoViewData = PhotoViewData(source)
            override fun newArray(size: Int): Array<PhotoViewData?> = arrayOfNulls(size)
        }
    }
}
package com.lwt.qmqiu.bean

import android.os.Parcel
import android.os.Parcelable


data class LocationInfo(var locationName: String, var locationWhere: String, var latitude: Double, var longitude: Double, var select: Boolean = false) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(locationName)
        writeString(locationWhere)
        writeDouble(latitude)
        writeDouble(longitude)
        writeInt((if (select) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocationInfo> = object : Parcelable.Creator<LocationInfo> {
            override fun createFromParcel(source: Parcel): LocationInfo = LocationInfo(source)
            override fun newArray(size: Int): Array<LocationInfo?> = arrayOfNulls(size)
        }
    }
}

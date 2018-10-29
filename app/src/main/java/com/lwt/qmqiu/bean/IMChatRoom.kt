package com.lwt.qmqiu.bean

import android.os.Parcel
import android.os.Parcelable

data class IMChatRoom(var _id:String?=null,
                      var currentCount:Int = 1,
                      var roomName:String="",
                      var roomNumber:String="",
                      var roomType:Int= 0,
                      var creatName:String="",
                      var lastContent:String="" ,
                      var latitude:Double=0.00,
                      var longitude:Double=0.00 ,
                      var creatTime:Long=System.currentTimeMillis(),
                      var lastContentTime:Long=System.currentTimeMillis(),
                      var status:Boolean = true) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeInt(currentCount)
        parcel.writeString(roomName)
        parcel.writeString(roomNumber)
        parcel.writeInt(roomType)
        parcel.writeString(creatName)
        parcel.writeString(lastContent)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeLong(creatTime)
        parcel.writeLong(lastContentTime)
        parcel.writeByte(if (status) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IMChatRoom> {
        override fun createFromParcel(parcel: Parcel): IMChatRoom {
            return IMChatRoom(parcel)
        }

        override fun newArray(size: Int): Array<IMChatRoom?> {
            return arrayOfNulls(size)
        }
    }


}
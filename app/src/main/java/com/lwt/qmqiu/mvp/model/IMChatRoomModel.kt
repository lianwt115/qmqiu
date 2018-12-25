package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class IMChatRoomModel(context: Context) : BaseModel(context) {


    fun getIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int): Observable<List<IMChatRoom>>?{

        return apiService?.getIMChatRoom(name, latitude,longitude,type)?.map(HttpResultFunc())

    }

    fun getIMChatRoomSearch(name:String,roomName:String, latitude:Double, longitude:Double,type:Int): Observable<IMChatRoom>?{

        return apiService?.getIMChatRoomSearch(name,roomName, latitude,longitude,type)?.map(HttpResultFunc())

    }

}
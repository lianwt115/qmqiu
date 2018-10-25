package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class IMChatRoomModel(context: Context) : BaseModel(context) {


    fun getIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int): Observable<List<IMChatRoom>>?{

        return apiService?.getIMChatRoom(name, latitude,longitude,type)?.map(HttpResultFunc())

    }
    fun creatIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int,roomName:String): Observable<IMChatRoom>?{

        return apiService?.creatIMChatRoom(name, latitude,longitude,type,roomName)?.map(HttpResultFunc())

    }


}
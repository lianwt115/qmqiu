package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class RoomMessageModel(context: Context) : BaseModel(context) {


    fun getRoomMessage(name:String, roomNumber:String): Observable<List<QMMessage>>?{

        return apiService?.getRoomMessage(name,roomNumber)?.map(HttpResultFunc())

    }
    fun reportUser(name:String, to: String, why:Int,roomNumber:String,messageContent:String, messageId:Long): Observable<Boolean>?{

        return apiService?.reportUser(name,to,why,roomNumber,messageContent,messageId)?.map(HttpResultFunc())

    }
    fun videoRequest(name:String, to: String,message:String): Observable<QMMessage>?{

        return apiService?.videoRequest(name,to,message)?.map(HttpResultFunc())

    }


}
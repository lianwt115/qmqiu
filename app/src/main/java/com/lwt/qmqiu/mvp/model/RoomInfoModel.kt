package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class RoomInfoModel(context: Context) : BaseModel(context) {


    fun getRoomActiveUser(name:String, roomNumber:String): Observable<List<BaseUser>>?{

        return apiService?.getRoomActiveUser(name,roomNumber)?.map(HttpResultFunc())

    }
    fun getRoomExitAndDelete(name:String, roomNumber:String): Observable<Boolean>?{

        return apiService?.getRoomExitAndDelete(name,roomNumber)?.map(HttpResultFunc())

    }


}
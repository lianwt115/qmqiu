package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class FaceVideoModel(context: Context) : BaseModel(context) {


    fun videoRequestExit(channelName:String,name:String,time:Int ): Observable<Boolean>?{

        return apiService?.videoRequestExit(channelName, name,time)?.map(HttpResultFunc())

    }

}
package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.network.HttpResultFunc
import com.lwt.qmqiu.network.RetrofitClient
import io.reactivex.Observable


open class BaseModel(val context: Context) {

    val API_VERSION = 1

    val retrofitClient = RetrofitClient.getInstance(context)
    val apiService = retrofitClient.create(ApiService::class.java)


    fun creatIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int,roomName:String): Observable<IMChatRoom>?{

        return apiService?.creatIMChatRoom(name, latitude,longitude,type,roomName)?.map(HttpResultFunc())

    }
}
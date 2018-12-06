package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.UploadLog
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.network.HttpResultFunc
import com.lwt.qmqiu.network.RetrofitClient
import io.reactivex.Observable
import okhttp3.MultipartBody


open class BaseModel(val context: Context) {

    val API_VERSION = 1

    val retrofitClient = RetrofitClient.getInstance(context)
    val apiService = retrofitClient.create(ApiService::class.java)


    fun creatIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int,roomName:String): Observable<IMChatRoom>?{

        return apiService?.creatIMChatRoom(name, latitude,longitude,type,roomName)?.map(HttpResultFunc())

    }

    fun refuseCheck(name:String, to:String): Observable<Boolean>?{

        return apiService?.refuseCheck(name,to)?.map(HttpResultFunc())

    }
    fun upload(from:String, type: Int, where:String,length:Int, file: MultipartBody.Part): Observable<UploadLog>?{

        return apiService?.upload(from,type,where,length,file)?.map(HttpResultFunc())

    }
    fun giftSend(name:String, to:String, giftIndex:Int,giftCount:Int): Observable<BaseUser>?{

        return apiService?.giftSend(name,to,giftIndex,giftCount)?.map(HttpResultFunc())

    }
}
package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class GiftLogModel(context: Context) : BaseModel(context) {


    fun getGiftLog(name:String,type:Int): Observable<List<GiftLog>>?{

        return apiService?.getGiftLog(name,type)?.map(HttpResultFunc())

    }



}
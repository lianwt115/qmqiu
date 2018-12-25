package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.CoinLog
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class CoinLogModel(context: Context) : BaseModel(context) {


    fun coinRecord(name:String,type:Int,all:Boolean): Observable<List<CoinLog>>?{

        return apiService?.coinRecord(name,type,all)?.map(HttpResultFunc())

    }

}
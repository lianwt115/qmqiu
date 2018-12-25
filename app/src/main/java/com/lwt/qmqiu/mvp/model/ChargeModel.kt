package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class ChargeModel(context: Context) : BaseModel(context) {


    fun creatChargeNum(name:String,type:Int,publickey:String, count:Int): Observable<String>?{

        return apiService?.creatChargeNum(name,type,publickey,count)?.map(HttpResultFunc())

    }

    fun coinCharge(name:String, chargenum:String): Observable<BaseUser>?{

        return apiService?.coinCharge(name,chargenum)?.map(HttpResultFunc())

    }



}
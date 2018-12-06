package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.RefuseLog
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class UserInfoModel(context: Context) : BaseModel(context) {


    fun userFind(name:String): Observable<BaseUser>?{

        return apiService?.userFind(name)?.map(HttpResultFunc())

    }
    fun giftBuy(name:String,cashCount:Int,giftCount:String,priceCount:String): Observable<BaseUser>?{

        return apiService?.giftBuy(name,cashCount,giftCount,priceCount)?.map(HttpResultFunc())

    }


    fun refuseUser(name:String, to: String, refuse:Boolean): Observable<RefuseLog>?{

        return apiService?.refuseUser(name,to,refuse)?.map(HttpResultFunc())

    }




}
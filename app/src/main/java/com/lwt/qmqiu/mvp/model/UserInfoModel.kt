package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class UserInfoModel(context: Context) : BaseModel(context) {


    fun userFind(name:String): Observable<BaseUser>?{

        return apiService?.userFind(name)?.map(HttpResultFunc())

    }
    fun giftBuy(name:String,cashCount:Int,giftCount:String,priceCount:String): Observable<BaseUser>?{

        return apiService?.giftBuy(name,cashCount,giftCount,priceCount)?.map(HttpResultFunc())

    }

    fun giftSend(name:String, to:String, giftIndex:Int,giftCount:Int): Observable<BaseUser>?{

        return apiService?.giftSend(name,to,giftIndex,giftCount)?.map(HttpResultFunc())

    }


}
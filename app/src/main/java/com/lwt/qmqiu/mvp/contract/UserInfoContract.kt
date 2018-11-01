package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.HttpResult
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import retrofit2.http.Query


interface UserInfoContract {

    interface View : BaseView<Presenter> {


        fun setUser(baseUser: BaseUser)
        fun setGiftBuy(baseUser: BaseUser)
        fun setGiftSend(baseUser: BaseUser)

        fun err(code:Int,errMessage:String?,type:Int)

    }
    interface Presenter : BasePresent {

        fun userFind(name:String,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun giftBuy(name:String,cashCount:Int,giftCount:String,priceCount:String,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun giftSend( name:String, to:String, giftIndex:Int,giftCount:Int,bindToLifecycle: LifecycleTransformer<BaseUser>)

    }
}
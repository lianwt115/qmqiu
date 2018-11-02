package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.HttpResult
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.RefuseLog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import retrofit2.http.Query


interface UserInfoContract {

    interface View : BaseView<Presenter> {


        fun setUser(baseUser: BaseUser)
        fun setRefuseUser(refuseLog: RefuseLog)
        fun setRefuseCheck(refuse: Boolean)
        fun setGiftBuy(baseUser: BaseUser)
        fun setGiftSend(baseUser: BaseUser)
        fun creatIMChatRoomSuccess(room:IMChatRoom)
        fun err(code:Int,errMessage:String?,type:Int)

    }

    interface Presenter : BasePresent {

        fun userFind(name:String,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun giftBuy(name:String,cashCount:Int,giftCount:String,priceCount:String,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun giftSend( name:String, to:String, giftIndex:Int,giftCount:Int,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun creatIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int,roomName:String,bindToLifecycle: LifecycleTransformer<IMChatRoom>)
        fun refuseUser(name:String, to: String, refuse:Boolean,bindToLifecycle: LifecycleTransformer<RefuseLog>)
        fun refuseCheck(name:String, to: String,bindToLifecycle: LifecycleTransformer<Boolean>)

    }
}
package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.CoinLog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView


interface ChargeContract {

    interface View : BaseView<Presenter> {

        fun setChargeNum(chargeNum:String)
        fun setCoinCharge(baseUser:BaseUser)

    }
    interface Presenter : BasePresent {

        fun creatChargeNum(name:String, type:Int, publickey:String, count:Int,bindToLifecycle: LifecycleTransformer<String>)
        fun coinCharge(name:String, chargenum:String,bindToLifecycle: LifecycleTransformer<BaseUser>)

    }
}
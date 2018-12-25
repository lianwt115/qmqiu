package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.CoinLog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView


interface CoinLogContract {

    interface View : BaseView<Presenter> {


        fun setCoinRecord(giftLog:List<CoinLog>)

    }
    interface Presenter : BasePresent {


        fun coinRecord(name:String,type:Int,all:Boolean,bindToLifecycle: LifecycleTransformer<List<CoinLog>>)

    }
}
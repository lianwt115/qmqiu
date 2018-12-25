package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.GiftLog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface GiftLogContract {

    interface View : BaseView<Presenter> {


        fun setGiftLog(giftLog:List<GiftLog>)

    }
    interface Presenter : BasePresent {


        fun getGiftLog(name:String,type:Int,bindToLifecycle: LifecycleTransformer<List<GiftLog>>)

    }
}
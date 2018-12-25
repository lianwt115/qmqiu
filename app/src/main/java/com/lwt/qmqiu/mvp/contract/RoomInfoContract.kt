package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface RoomInfoContract {

    interface View : BaseView<Presenter> {


        fun setActiveUser(activeList:List<BaseUser>)
        fun setExitAndDelete(success:Boolean)


    }

    interface Presenter : BasePresent {

        fun getActiveUser(name:String, roomNumber:String,bindToLifecycle: LifecycleTransformer<List<BaseUser>>)
        fun exitAndDelete(name:String, roomNumber:String,bindToLifecycle: LifecycleTransformer<Boolean>)

    }
}
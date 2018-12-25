package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface EditContract {

    interface View : BaseView<Presenter> {

        fun setUser(user:BaseUser)

    }

    interface Presenter : BasePresent {

         fun updataUser(name:String, showname:String,age: Int, male: Boolean,imgpath: String,bindToLifecycle: LifecycleTransformer<BaseUser>)

    }
}
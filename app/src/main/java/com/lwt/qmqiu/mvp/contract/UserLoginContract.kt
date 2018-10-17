package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.HttpResult
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import retrofit2.http.Query


interface UserLoginContract {

    interface View : BaseView<Presenter> {

        fun registSuccess(baseUser: BaseUser)
        fun err(code:Int,errMessage:String?)

    }
    interface Presenter : BasePresent {

        fun userRegist(name:String,password:String,bindToLifecycle: LifecycleTransformer<BaseUser>)


    }
}
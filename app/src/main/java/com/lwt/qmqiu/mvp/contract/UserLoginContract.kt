package com.lwt.qmqiu.mvp.contract


import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface UserLoginContract {

    interface View : BaseView<Presenter> {

    }
    interface Presenter : BasePresent {


    }
}
package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface UserLoginContract {

    interface View : BaseView<Presenter> {


        fun successRegistOrLogin(baseUser: BaseUser,regist:Boolean)

    }
    interface Presenter : BasePresent {

        fun userRegist(name:String,password:String,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun userLogin(name:String,password:String,auto:Boolean, loginWhere:String, latitude:Double, longitude:Double,bindToLifecycle: LifecycleTransformer<BaseUser>)
        fun userLoginOut(name:String,password:String,auto:Boolean, loginWhere:String, latitude:Double, longitude:Double,bindToLifecycle: LifecycleTransformer<Boolean>)

    }
}
package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface FaceVideoContract {

    interface View : BaseView<Presenter> {


        fun setEexitVideoRequest(success:Boolean)

        fun err(code:Int,errMessage:String?,type:Int)

    }
    interface Presenter : BasePresent {


        fun exitVideoRequest(channelName:String,name:String,time:Int)

    }
}
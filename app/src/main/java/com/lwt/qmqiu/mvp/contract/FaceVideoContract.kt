package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface FaceVideoContract {

    interface View : BaseView<Presenter> {


        fun setEexitVideoRequest(success:Boolean)


    }
    interface Presenter : BasePresent {


        fun exitVideoRequest(channelName:String,name:String,time:Int)

    }
}
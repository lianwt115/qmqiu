package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import retrofit2.http.Query


interface RoomMessageContract {

    interface View : BaseView<Presenter> {

        fun setRefuseCheck(refuse: Boolean)
        fun setReportUser(success: Boolean)
        fun setRoomMessage(messageList:List<QMMessage>)

        fun err(code:Int,errMessage:String?,type:Int)

    }
    interface Presenter : BasePresent {

        fun getRoomMessage(name:String, roomNumber:String,bindToLifecycle: LifecycleTransformer<List<QMMessage>>)
        fun refuseCheck(name:String, to: String,bindToLifecycle: LifecycleTransformer<Boolean>)
        fun reportUser(name:String, to: String, why:Int,roomNumber:String,messageContent:String, messageId:Long,bindToLifecycle: LifecycleTransformer<Boolean>)

    }
}
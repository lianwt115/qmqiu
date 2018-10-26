package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface RoomMessageContract {

    interface View : BaseView<Presenter> {


        fun setRoomMessage(messageList:List<QMMessage>)

        fun err(code:Int,errMessage:String?,type:Int)

    }
    interface Presenter : BasePresent {

        fun getRoomMessage(name:String, roomNumber:String,bindToLifecycle: LifecycleTransformer<List<QMMessage>>)

    }
}
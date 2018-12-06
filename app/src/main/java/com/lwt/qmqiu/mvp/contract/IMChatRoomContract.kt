package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.IMChatRoom
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface IMChatRoomContract {

    interface View : BaseView<Presenter> {


        fun setIMChatRoom(roomList:List<IMChatRoom>)
        fun creatIMChatRoomSuccess(room:IMChatRoom)


    }
    interface Presenter : BasePresent {

        fun getIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int,bindToLifecycle: LifecycleTransformer<List<IMChatRoom>>)
        fun creatIMChatRoom(name:String, latitude:Double, longitude:Double,type:Int,roomName:String,bindToLifecycle: LifecycleTransformer<IMChatRoom>)

    }
}
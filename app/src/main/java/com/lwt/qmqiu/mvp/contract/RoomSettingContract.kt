package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.IMChatRoom
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface RoomSettingContract {

    interface View : BaseView<Presenter> {


        fun setRoomCreatByMe(roomList:List<IMChatRoom>)

    }
    interface Presenter : BasePresent {


        fun getRoomCreatByMe(name:String,bindToLifecycle: LifecycleTransformer<List<IMChatRoom>>)

    }
}
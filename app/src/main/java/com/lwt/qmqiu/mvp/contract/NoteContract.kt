package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.bean.QMMessage
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import retrofit2.http.Query


interface NoteContract {

    interface View : BaseView<Presenter> {


        fun setNote(noteList:List<NoteLog>)


    }

    interface Presenter : BasePresent {


        fun getNote(name:String,noteType:Int,latitude:Double, longitude:Double,bindToLifecycle: LifecycleTransformer<List<NoteLog>>)

    }
}
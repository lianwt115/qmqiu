package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.NoteLog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView



interface NoteMineContract {

    interface View : BaseView<Presenter> {


        fun setNoteMine(noteList:List<NoteLog>)
        fun setDeleteNoteMine(success:Boolean,position: Int)

    }

    interface Presenter : BasePresent {

        fun getNoteMine(name:String,bindToLifecycle: LifecycleTransformer<List<NoteLog>>)
        fun deleteNoteMine(name:String,id:String,position:Int,bindToLifecycle: LifecycleTransformer<Boolean>)


    }
}
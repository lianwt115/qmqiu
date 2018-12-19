package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.*
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import retrofit2.http.Query


interface NoteContract {

    interface View : BaseView<Presenter> {


        fun setNote(noteList:List<NoteLog>)
        fun setGoodNote(success:Boolean,position:Int)
        fun setReportNote(success:Boolean)


        fun setCreateComment(noteCommentLog:NoteCommentLog)
        fun setGetComment(commentList:List<NoteCommentLog>,id: String)
        fun setDeleteComment(success: Boolean,position: Int)
    }

    interface Presenter : BasePresent {


        fun getNote(name:String,noteType:Int,latitude:Double, longitude:Double,bindToLifecycle: LifecycleTransformer<List<NoteLog>>)
        fun goodNote(name:String,id:String,position:Int,bindToLifecycle: LifecycleTransformer<Boolean>)
        fun reportNote(name:String,id:String,why:Int,bindToLifecycle: LifecycleTransformer<Boolean>)

        fun deleteComment(name:String,id:String,position: Int,bindToLifecycle: LifecycleTransformer<Boolean>)
        fun createComment(name:String,id: String,commemtText:String,bindToLifecycle: LifecycleTransformer<NoteCommentLog>)
        fun getComment(name:String,id:String,bindToLifecycle: LifecycleTransformer<List<NoteCommentLog>>)


    }
}
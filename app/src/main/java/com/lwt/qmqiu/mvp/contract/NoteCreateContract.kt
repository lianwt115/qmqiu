package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.*
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import okhttp3.MultipartBody
import retrofit2.http.Query


interface NoteCreateContract {

    interface View : BaseView<Presenter> {


        fun setCreateNote(success:Boolean)
        fun setUpload(uploadLog: UploadLog)
    }

    interface Presenter : BasePresent {


        fun createNote(name:String,noteType:Int,seeType:Int,topic:String, textContent:String,imgList:String,latitude:Double, longitude:Double,where:String,bindToLifecycle: LifecycleTransformer<Boolean>)
        fun upload(from:String, type: Int, where:String, length:Int, file: MultipartBody.Part, bindToLifecycle: LifecycleTransformer<UploadLog>)

    }
}
package com.lwt.qmqiu.mvp.contract


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.UploadLog
import com.trello.rxlifecycle2.LifecycleTransformer
import com.lwt.qmqiu.mvp.base.BasePresent
import com.lwt.qmqiu.mvp.base.BaseView
import okhttp3.MultipartBody



interface RoomMessageContract {

    interface View : BaseView<Presenter> {

        fun setRefuseCheck(refuse: Boolean)
        fun setReportUser(success: Boolean)
        fun setRoomMessage(messageList:List<QMMessage>)
        fun setUpload(uploadLog: UploadLog)
        fun setVideoRequest(videoChannel: QMMessage)
        fun setGiftSend(baseUser: BaseUser,giftIndex: Int)

    }
    interface Presenter : BasePresent {

        fun getRoomMessage(name:String, roomNumber:String,bindToLifecycle: LifecycleTransformer<List<QMMessage>>)
        fun refuseCheck(name:String, to: String,bindToLifecycle: LifecycleTransformer<Boolean>)
        fun reportUser(name:String, to: String, why:Int,roomNumber:String,messageContent:String, messageId:Long,bindToLifecycle: LifecycleTransformer<Boolean>)
        fun upload(from:String, type: Int, where:String,length:Int, file: MultipartBody.Part, bindToLifecycle: LifecycleTransformer<UploadLog>)
        fun videoRequest(from:String,to: String,message: String, bindToLifecycle: LifecycleTransformer<QMMessage>)
        fun giftSend( name:String, to:String, giftIndex:Int,giftCount:Int,bindToLifecycle: LifecycleTransformer<BaseUser>)

    }
}
package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.UploadLog
import com.lwt.qmqiu.mvp.contract.IMChatRoomContract
import com.lwt.qmqiu.mvp.contract.RoomMessageContract
import com.lwt.qmqiu.mvp.model.IMChatRoomModel
import com.lwt.qmqiu.mvp.model.RoomMessageModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable
import okhttp3.MultipartBody


class RoomMessagePresent(context: Context, view: RoomMessageContract.View) : RoomMessageContract.Presenter{


    private var mContext : Context = context
    private var mView : RoomMessageContract.View = view
    private val mModel : RoomMessageModel by lazy {
        RoomMessageModel(context)
    }


    override fun getRoomMessage(name: String, roomNumber: String, bindToLifecycle: LifecycleTransformer<List<QMMessage>>) {
        val observable : Observable<List<QMMessage>>? = mContext.let {
            mModel.getRoomMessage(name,roomNumber) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setRoomMessage(it)
                }, {

            Logger.e(it.message?:"错误消息为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,1)

            }else{
                mView?.err(-1,it.message,1)
            }

        }

        )
    }

    override fun refuseCheck(name: String, to: String, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.refuseCheck(name,to) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setRefuseCheck(it)
                }, {

            Logger.e(it.message?:"错误为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,2)

            }else{
                mView?.err(-1,it.message,2)
            }

        }

        )

    }

    override fun reportUser(name: String, to: String, why: Int, roomNumber: String, messageContent: String, messageId: Long, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.reportUser(name,to,why,roomNumber,messageContent,messageId) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setReportUser(it)
                }, {

            Logger.e(it.message?:"错误为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,3)

            }else{
                mView?.err(-1,it.message,3)
            }

        }

        )
    }

    override fun upload(from: String, type: Int, where: String, length: Int, file: MultipartBody.Part, bindToLifecycle: LifecycleTransformer<UploadLog>) {
        val observable : Observable<UploadLog>? = mContext.let {
            mModel.upload(from,type,where,length,file) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setUpload(it)
                }, {

            Logger.e(it.message?:"错误为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,4)

            }else{
                mView?.err(-1,it.message,4)
            }

        }

        )
    }

    override fun videoRequest(from: String, to: String, message: String, bindToLifecycle: LifecycleTransformer<QMMessage>) {
        val observable : Observable<QMMessage>? = mContext.let {
            mModel.videoRequest(from,to,message) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setVideoRequest(it)
                }, {

            Logger.e(it.message?:"错误为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,5)

            }else{
                mView?.err(-1,it.message,5)
            }

        }

        )
    }

    override fun giftSend(name: String, to: String, giftIndex: Int, giftCount: Int, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext.let {
            mModel.giftSend(name,to,giftIndex,giftCount) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setGiftSend(it,giftIndex)
                }, {

            Logger.e(it.message?:"错误消息为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,6)

            }else{
                mView?.err(-1,it.message,6)
            }

        }

        )
    }

}
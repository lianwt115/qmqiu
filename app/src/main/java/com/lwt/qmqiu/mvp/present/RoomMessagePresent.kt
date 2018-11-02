package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.mvp.contract.IMChatRoomContract
import com.lwt.qmqiu.mvp.contract.RoomMessageContract
import com.lwt.qmqiu.mvp.model.IMChatRoomModel
import com.lwt.qmqiu.mvp.model.RoomMessageModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class RoomMessagePresent(context: Context, view: RoomMessageContract.View) : RoomMessageContract.Presenter{



    var mContext : Context? = null
    var mView : RoomMessageContract.View? = null
    val mModel : RoomMessageModel by lazy {
        RoomMessageModel(context)
    }
    init {
        mView = view
        mContext = context
    }

    override fun getRoomMessage(name: String, roomNumber: String, bindToLifecycle: LifecycleTransformer<List<QMMessage>>) {
        val observable : Observable<List<QMMessage>>? = mContext?.let {
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
        val observable : Observable<Boolean>? = mContext?.let {
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
        val observable : Observable<Boolean>? = mContext?.let {
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

}
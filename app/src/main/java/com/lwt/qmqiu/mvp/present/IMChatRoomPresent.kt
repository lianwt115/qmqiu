package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.mvp.contract.IMChatRoomContract
import com.lwt.qmqiu.mvp.contract.UserLoginContract
import com.lwt.qmqiu.mvp.model.IMChatRoomModel
import com.lwt.qmqiu.mvp.model.UserLoginModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class IMChatRoomPresent(context: Context, view: IMChatRoomContract.View) : IMChatRoomContract.Presenter{



    private var mContext : Context = context
    private var mView : IMChatRoomContract.View = view
    private val mModel : IMChatRoomModel by lazy {
        IMChatRoomModel(context)
    }


    override fun getIMChatRoom(name: String, latitude: Double, longitude: Double, type: Int, bindToLifecycle: LifecycleTransformer<List<IMChatRoom>>) {

        val observable : Observable<List<IMChatRoom>>? = mContext.let {
            mModel.getIMChatRoom(name,latitude,longitude,type) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setIMChatRoom(it)
                }, {

            Logger.e(it.message?:"错误为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,1)

            }else{
                mView?.err(-1,it.message,1)
            }

        }

        )

    }
    override fun creatIMChatRoom(name: String, latitude: Double, longitude: Double, type: Int, roomName: String, bindToLifecycle: LifecycleTransformer<IMChatRoom>) {
        val observable : Observable<IMChatRoom>? = mContext.let {
            mModel.creatIMChatRoom(name,latitude,longitude,type,roomName) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.creatIMChatRoomSuccess(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,2)

            }else{
                mView?.err(-1,it.message,2)
            }

        }

        )
    }

}
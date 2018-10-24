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



    var mContext : Context? = null
    var mView : IMChatRoomContract.View? = null
    val mModel : IMChatRoomModel by lazy {
        IMChatRoomModel(context)
    }
    init {
        mView = view
        mContext = context
    }

    override fun getIMChatRoom(name: String, latitude: Double, longitude: Double, type: Int, bindToLifecycle: LifecycleTransformer<List<IMChatRoom>>) {

        val observable : Observable<List<IMChatRoom>>? = mContext?.let {
            mModel.getIMChatRoom(name,latitude,longitude,type) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setIMChatRoom(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message)

            }else{
                mView?.err(-1,it.message)
            }

        }

        )

    }

}
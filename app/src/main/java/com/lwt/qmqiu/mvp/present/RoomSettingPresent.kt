package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.mvp.contract.RoomInfoContract
import com.lwt.qmqiu.mvp.contract.RoomSettingContract
import com.lwt.qmqiu.mvp.model.RoomInfoModel
import com.lwt.qmqiu.mvp.model.RoomSettingModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class RoomSettingPresent(context: Context, view: RoomSettingContract.View) : RoomSettingContract.Presenter{



    private var mContext : Context = context
    private var mView : RoomSettingContract.View = view
    private val mModel : RoomSettingModel by lazy {
        RoomSettingModel(context)
    }

    override fun getRoomCreatByMe(name: String, bindToLifecycle: LifecycleTransformer<List<IMChatRoom>>) {
        val observable : Observable<List<IMChatRoom>>? = mContext.let {
            mModel.getRoomCreatByMe(name) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setRoomCreatByMe(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,1)

            }else{
                mView?.err(-1,it.message,1)
            }

        }

        )
    }


}
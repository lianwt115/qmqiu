package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.RoomInfoContract
import com.lwt.qmqiu.mvp.model.RoomInfoModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class RoomInfoPresent(context: Context, view: RoomInfoContract.View) : RoomInfoContract.Presenter{



    var mContext : Context? = null
    var mView : RoomInfoContract.View? = null
    val mModel : RoomInfoModel by lazy {
        RoomInfoModel(context)
    }
    init {
        mView = view
        mContext = context
    }



    override fun getActiveUser(name: String, roomNumber: String, bindToLifecycle: LifecycleTransformer<List<BaseUser>>) {
        val observable : Observable<List<BaseUser>>? = mContext?.let {
            mModel.getRoomActiveUser(name,roomNumber) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setActiveUser(it)
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

    override fun exitAndDelete(name: String, roomNumber: String, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext?.let {
            mModel.getRoomExitAndDelete(name,roomNumber) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setExitAndDelete(it)
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
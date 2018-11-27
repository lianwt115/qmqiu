package com.lwt.qmqiu.mvp.present

import android.annotation.SuppressLint
import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.FaceVideoContract
import com.lwt.qmqiu.mvp.contract.RoomInfoContract
import com.lwt.qmqiu.mvp.model.FaceVideoModel
import com.lwt.qmqiu.mvp.model.RoomInfoModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class FaceVideoPresent(context: Context, view: FaceVideoContract.View) : FaceVideoContract.Presenter{



    var mContext : Context = context
    var mView : FaceVideoContract.View = view
    val mModel : FaceVideoModel by lazy {
        FaceVideoModel(context)
    }




    override fun exitVideoRequest(channelName: String, name: String, time: Int) {

        val observable : Observable<Boolean>? = mContext?.let {
            mModel.videoRequestExit(channelName,name,time) }


        observable?.applySchedulers()?.subscribe(

                {
                    Logger.e("退出房间:$it")
                }, {

            Logger.e(it.message)

        }

        )

    }


}
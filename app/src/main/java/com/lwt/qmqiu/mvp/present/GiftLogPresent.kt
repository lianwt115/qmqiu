package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.mvp.contract.GiftLogContract
import com.lwt.qmqiu.mvp.model.GiftLogModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class GiftLogPresent(context: Context, view: GiftLogContract.View) : GiftLogContract.Presenter{


    private var mContext : Context = context
    private var mView : GiftLogContract.View = view
    private val mModel : GiftLogModel by lazy {
        GiftLogModel(context)
    }

    override fun getGiftLog(name: String, type: Int, bindToLifecycle: LifecycleTransformer<List<GiftLog>>) {
        val observable : Observable<List<GiftLog>>? = mContext.let {
            mModel.getGiftLog(name,type) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setGiftLog(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,type)

            }else{
                mView?.err(-1,it.message,type)
            }

        }

        )
    }

}
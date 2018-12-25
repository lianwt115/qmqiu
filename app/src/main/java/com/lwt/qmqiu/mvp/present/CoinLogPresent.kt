package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.CoinLog
import com.lwt.qmqiu.mvp.contract.CoinLogContract
import com.lwt.qmqiu.mvp.model.CoinLogModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class CoinLogPresent(context: Context, view: CoinLogContract.View) : CoinLogContract.Presenter{



    private var mContext : Context = context
    private var mView : CoinLogContract.View = view
    private val mModel : CoinLogModel by lazy {
        CoinLogModel(context)
    }

    override fun coinRecord(name: String, type: Int, all: Boolean, bindToLifecycle: LifecycleTransformer<List<CoinLog>>) {
        val observable : Observable<List<CoinLog>>? = mContext.let {
            mModel.coinRecord(name,type,all) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setCoinRecord(it)
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
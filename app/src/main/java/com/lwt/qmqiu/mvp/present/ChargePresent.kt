package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.ChargeContract
import com.lwt.qmqiu.mvp.model.ChargeModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class ChargePresent(context: Context, view: ChargeContract.View) : ChargeContract.Presenter{



    private var mContext : Context = context
    private var mView : ChargeContract.View = view
    private val mModel : ChargeModel by lazy {
        ChargeModel(context)
    }

    override fun creatChargeNum(name: String, type: Int, publickey: String, count: Int, bindToLifecycle: LifecycleTransformer<String>) {
        val observable : Observable<String>? = mContext.let {
            mModel.creatChargeNum(name,type,publickey,count) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setChargeNum(it)
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

    override fun coinCharge(name: String, chargenum: String, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext.let {
            mModel.coinCharge(name,chargenum) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setCoinCharge(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,3)

            }else{
                mView?.err(-1,it.message,3)
            }

        }

        )
    }

}
package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.UserInfoContract
import com.lwt.qmqiu.mvp.model.UserInfoModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class UserInfoPresent(context: Context, view: UserInfoContract.View) : UserInfoContract.Presenter{



    var mContext : Context? = null
    var mView : UserInfoContract.View? = null
    val mModel : UserInfoModel by lazy {
        UserInfoModel(context)
    }
    init {
        mView = view
        mContext = context
    }

    override fun userFind(name: String, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext?.let {
            mModel.userFind(name) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setUser(it)
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


    override fun giftBuy(name: String, cashCount: Int, giftCount: String, priceCount: String, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext?.let {
            mModel.giftBuy(name,cashCount,giftCount,priceCount) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setGiftBuy(it)
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

    override fun giftSend(name: String, to: String, giftIndex: Int, giftCount: Int, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext?.let {
            mModel.giftSend(name,to,giftIndex,giftCount) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setGiftSend(it)
                }, {

            Logger.e(it.message?:"错误消息为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,3)

            }else{
                mView?.err(-1,it.message,3)
            }

        }

        )
    }
}
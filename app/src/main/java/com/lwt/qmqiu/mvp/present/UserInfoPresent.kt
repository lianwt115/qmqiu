package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.RefuseLog
import com.lwt.qmqiu.mvp.contract.UserInfoContract
import com.lwt.qmqiu.mvp.model.UserInfoModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class UserInfoPresent(context: Context, view: UserInfoContract.View) : UserInfoContract.Presenter{


    private var mContext : Context = context
    private var mView : UserInfoContract.View = view
    private val mModel : UserInfoModel by lazy {
        UserInfoModel(context)
    }

    override fun userFind(name: String, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext.let {
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
        val observable : Observable<BaseUser>? = mContext.let {
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
        val observable : Observable<BaseUser>? = mContext.let {
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

    override fun creatIMChatRoom(name: String, latitude: Double, longitude: Double, type: Int, roomName: String, bindToLifecycle: LifecycleTransformer<IMChatRoom>) {
        val observable : Observable<IMChatRoom>? = mContext.let {
            mModel.creatIMChatRoom(name,latitude,longitude,type,roomName) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.creatIMChatRoomSuccess(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,4)

            }else{
                mView?.err(-1,it.message,4)
            }

        }

        )
    }

    override fun refuseUser(name: String, to: String, refuse: Boolean, bindToLifecycle: LifecycleTransformer<RefuseLog>) {
        val observable : Observable<RefuseLog>? = mContext.let {
            mModel.refuseUser(name,to,refuse) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setRefuseUser(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,5)

            }else{
                mView?.err(-1,it.message,5)
            }

        }

        )
    }

    override fun refuseCheck(name: String, to: String, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.refuseCheck(name,to) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setRefuseCheck(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,6)

            }else{

                mView?.err(-1,it.message,6)

            }

        }

        )
    }


}
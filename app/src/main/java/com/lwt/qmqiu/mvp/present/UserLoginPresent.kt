package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.UserLoginContract
import com.lwt.qmqiu.mvp.model.UserLoginModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class UserLoginPresent(context: Context, view: UserLoginContract.View) : UserLoginContract.Presenter{


    var mContext : Context? = null
    var mView : UserLoginContract.View? = null
    val mModel : UserLoginModel by lazy {
        UserLoginModel(context)
    }
    init {
        mView = view
        mContext = context
    }


    override fun userRegist(name: String, password: String, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext?.let {
            mModel.userRegist(name,password) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.successRegistOrLogin(it,true)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,0)

            }else{
                mView?.err(-1,it.message,0)
            }

        }

        )
    }

    override fun userLogin(name: String, password: String, auto: Boolean, loginWhere: String, latitude: Double, longitude: Double, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext?.let {
            mModel.userLogin(name,password,auto,loginWhere, latitude, longitude) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.successRegistOrLogin(it,false)
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

    override fun userLoginOut(name: String, password: String, auto: Boolean, loginWhere: String, latitude: Double, longitude: Double, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext?.let {
            mModel.userLoginOut(name,password,auto,loginWhere, latitude, longitude) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    Logger.e("自动登出:$it")
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
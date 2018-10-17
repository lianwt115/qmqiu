package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.UserLoginContract
import com.lwt.qmqiu.mvp.model.UserLoginModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.netease.nimlib.sdk.uinfo.model.UserInfo
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
                    mView?.registSuccess(it)
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



   /* override fun searchCardAddress(cardname: String, cardid: String, cardtype: String, bindToLifecycle: LifecycleTransformer<UserInfo>) {

        val observable : Observable<UserInfo>? = mContext?.let {
            mModel.searchCardAddress(cardname,cardid,cardtype) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setSearchCardAddress(it)
                }, {

            Logger.e(it.message)
            if (it is ApiException){

                mView?.err(it.getResultCode().toString(),0,cardtype)

            }else{
                mView?.err("-1",0,cardtype)
            }

        }

        )

    }

    override fun getPersonNumber(cardname: String, cardid: String, cardtype: String, bindToLifecycle: LifecycleTransformer<UserInfo>) {

        val observable : Observable<UserInfo>? = mContext?.let {
            mModel.getPersonNumber(cardname,cardid,cardtype) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setGetPersonNumber(it)
                }, {

            Logger.e(it.message)
            if (it is ApiException){

                mView?.err(it.getResultCode().toString(),1,cardtype)

            }else{
                mView?.err("-1",1,cardtype)
            }

        }

        )

    }

    override fun getUpdata(version: Int, bindToLifecycle: LifecycleTransformer<ApkInfo>) {
        val observable : Observable<ApkInfo>? = mContext?.let {
            mModel.updata(version) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setUpdata(it)
                }, {

            Logger.e(it.message)
            if (it is ApiException){

                mView?.err(it.getResultCode().toString(),2,"")

            }else{
                mView?.err("-1",2,"")
            }

        }

        )
    }*/

}
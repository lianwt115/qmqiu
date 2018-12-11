package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.EditContract
import com.lwt.qmqiu.mvp.model.EditModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class EditPresent(context: Context, view: EditContract.View) : EditContract.Presenter{



    private var mContext : Context = context
    private var mView : EditContract.View = view
    private val mModel : EditModel by lazy {
        EditModel(context)
    }

    override fun updataUser(name: String, showname: String, age: Int, male: Boolean, imgpath: String, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext.let {
            mModel.userUpdata(name,showname,age,male,imgpath) }


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

}
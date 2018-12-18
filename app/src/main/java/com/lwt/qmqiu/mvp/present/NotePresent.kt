package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.mvp.contract.FaceVideoContract
import com.lwt.qmqiu.mvp.contract.NoteContract
import com.lwt.qmqiu.mvp.model.FaceVideoModel
import com.lwt.qmqiu.mvp.model.NoteModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class NotePresent(context: Context, view: NoteContract.View) : NoteContract.Presenter{



    private  var mContext : Context = context
    private var mView : NoteContract.View = view
    private val mModel : NoteModel by lazy {
        NoteModel(context)
    }

    override fun getNote(name: String, noteType: Int, latitude: Double, longitude: Double, bindToLifecycle: LifecycleTransformer<List<NoteLog>>) {
        val observable : Observable<List<NoteLog>>? = mContext.let {
            mModel.getNote(name, noteType, latitude, longitude) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setNote(it)
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
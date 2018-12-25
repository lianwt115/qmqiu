package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.NoteCommentLog
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.mvp.contract.NoteContract
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

    override fun goodNote(name: String, id: String, position: Int, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.goodNote(name,id) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setGoodNote(it,position)
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

    override fun reportNote(name: String, id: String, why: Int, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.reportNote(name,id,why) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setReportNote(it)
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

    override fun createComment(name: String, id: String, commemtText: String, bindToLifecycle: LifecycleTransformer<NoteCommentLog>) {
        val observable : Observable<NoteCommentLog>? = mContext.let {
            mModel.createComment(name,id,commemtText) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setCreateComment(it)
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

    override fun getComment(name: String, id: String, bindToLifecycle: LifecycleTransformer<List<NoteCommentLog>>) {
        val observable : Observable<List<NoteCommentLog>>? = mContext.let {
            mModel.getComment(name,id) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setGetComment(it,id)
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

    override fun deleteComment(name: String, id: String, position: Int, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.deleteComment(name,id) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setDeleteComment(it,position)
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

    override fun reportComment(name: String, id: String, why: Int, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.reportComment(name,id,why) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setReportComment(it)
                }, {

            Logger.e(it.message)

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,7)

            }else{
                mView?.err(-1,it.message,7)
            }

        }

        )
    }


}
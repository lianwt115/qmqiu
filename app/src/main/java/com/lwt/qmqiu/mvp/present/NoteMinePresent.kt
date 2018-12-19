package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.mvp.contract.NoteMineContract
import com.lwt.qmqiu.mvp.model.NoteMineModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class NoteMinePresent(context: Context, view: NoteMineContract.View) : NoteMineContract.Presenter{



    private  var mContext : Context = context
    private var mView : NoteMineContract.View = view
    private val mModel : NoteMineModel by lazy {
        NoteMineModel(context)
    }

   override fun getNoteMine(name: String, bindToLifecycle: LifecycleTransformer<List<NoteLog>>) {
       val observable : Observable<List<NoteLog>>? = mContext.let {
           mModel.getMineNote(name) }


       observable?.applySchedulers()?.subscribe(

               {
                   mView.setNoteMine(it)
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
    override fun deleteNoteMine(name: String, id: String, position: Int, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.deleteMineNote(name,id) }


        observable?.applySchedulers()?.subscribe(

                {
                    mView.setDeleteNoteMine(it,position)
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
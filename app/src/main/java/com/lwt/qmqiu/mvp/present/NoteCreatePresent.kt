package com.lwt.qmqiu.mvp.present

import android.content.Context
import com.lwt.qmqiu.bean.UploadLog
import com.lwt.qmqiu.mvp.contract.NoteCreateContract
import com.lwt.qmqiu.mvp.model.NoteCreateModel
import com.lwt.qmqiu.network.ApiException
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable
import okhttp3.MultipartBody


class NoteCreatePresent(context: Context, view: NoteCreateContract.View) : NoteCreateContract.Presenter{


    private  var mContext : Context = context
    private var mView : NoteCreateContract.View = view
    private val mModel : NoteCreateModel by lazy {
        NoteCreateModel(context)
    }


    override fun createNote(name: String, noteType: Int, seeType: Int, topic: String, textContent: String, imgList: String, latitude: Double, longitude: Double, where: String, bindToLifecycle: LifecycleTransformer<Boolean>) {
        val observable : Observable<Boolean>? = mContext.let {
            mModel.createNote(name, noteType, seeType, topic,textContent,imgList,latitude,longitude,where) }

        observable?.applySchedulers()?.subscribe(

                {
                    mView.setCreateNote(it)
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

    override fun upload(from: String, type: Int, where: String, length: Int, file: MultipartBody.Part, bindToLifecycle: LifecycleTransformer<UploadLog>) {
        val observable : Observable<UploadLog>? = mContext.let {
            mModel.upload(from,type,where,length,file) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.setUpload(it)
                }, {

            Logger.e(it.message?:"错误为空")

            if (it is ApiException){

                mView?.err(it.getResultCode()!!,it.message,2)

            }else{
                mView?.err(-1,it.message,2)
            }

        }

        )
    }


}
package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class NoteMineModel(context: Context) : BaseModel(context) {


    fun getMineNote(name:String): Observable<List<NoteLog>>?{

        return apiService?.noteGetMine(name)?.map(HttpResultFunc())

    }

    fun deleteMineNote(name:String,id:String): Observable<Boolean>?{

        return apiService?.noteDelete(name,id)?.map(HttpResultFunc())

    }



}
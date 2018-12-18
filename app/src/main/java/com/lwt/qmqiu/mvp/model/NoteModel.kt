package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class NoteModel(context: Context) : BaseModel(context) {


    fun getNote(name:String,noteType:Int,latitude:Double, longitude:Double): Observable<List<NoteLog>>?{

        return apiService?.noteGet(name,noteType,latitude,longitude)?.map(HttpResultFunc())

    }

}
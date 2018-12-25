package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class NoteCreateModel(context: Context) : BaseModel(context) {


    fun createNote(name:String,noteType:Int,seeType:Int,topic:String,textContent:String,imgList:String,latitude:Double, longitude:Double,where:String): Observable<Boolean>?{

        return apiService?.noteCreate(name,noteType,seeType,topic,textContent,imgList,latitude,longitude,where)?.map(HttpResultFunc())

    }

}
package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class EditModel(context: Context) : BaseModel(context) {


    fun userUpdata(name:String, showname:String,age: Int, male: Boolean,imgpath: String): Observable<BaseUser>?{

        return apiService?.userUpdata(name,showname,age,male,imgpath)?.map(HttpResultFunc())

    }



}
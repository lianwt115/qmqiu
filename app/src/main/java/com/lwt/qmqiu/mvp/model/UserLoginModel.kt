package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.network.HttpResultFunc
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class UserLoginModel(context: Context) : BaseModel(context) {


    fun userRegist(name:String,password:String): Observable<BaseUser>?{

        return apiService?.userRegist(name, password)?.map(HttpResultFunc())

    }
    fun userLogin(name:String,password:String,auto:Boolean,loginWhere:String, latitude:Double, longitude:Double): Observable<BaseUser>?{

        return apiService?.userLogin(name, password,auto,loginWhere, latitude, longitude)?.map(HttpResultFunc())

    }
    fun userLoginOut(name:String,password:String,auto:Boolean,loginWhere:String, latitude:Double, longitude:Double): Observable<Boolean>?{

        return apiService?.userLoginOut(name, password,auto,loginWhere, latitude, longitude)?.map(HttpResultFunc())

    }


}
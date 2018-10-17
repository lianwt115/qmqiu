package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.network.HttpResultFunc
import com.netease.nimlib.sdk.uinfo.model.UserInfo
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class UserLoginModel(context: Context) : BaseModel(context) {


    fun userRegist(name:String,password:String): Observable<BaseUser>?{

        return apiService?.userRegist(name, password)?.map(HttpResultFunc())

    }















    /* //cardtype  6：社保卡
     // 9：手输身份
     fun searchCardAddress(cardname: String,cardid: String,cardtype: String,hospitalnum: String="",terminalnum: String=""): Observable<UserInfo>? {

         //测试
         return Observable.create<UserInfo> {

             it.onNext(UserInfo("LWT","11111"))
             it.onComplete()

         }




         //return apiService?.searchCardAddress(cardname, cardid,hospitalnum,terminalnum,cardtype)?.map(HttpResultFunc<UserInfo>())

     }
     fun getPersonNumber(cardname: String,cardid: String,cardtype: String,hospitalnum: String="",terminalnum: String=""): Observable<UserInfo>? {

         return apiService?.getPersonNumber(cardname, cardid,hospitalnum,terminalnum,cardtype)?.map(HttpResultFunc<UserInfo>())

     }

     fun updata(version: Int): Observable<ApkInfo>? {

         return apiService?.getUpdata(version)?.map(HttpResultFunc<ApkInfo>())

     }*/
}
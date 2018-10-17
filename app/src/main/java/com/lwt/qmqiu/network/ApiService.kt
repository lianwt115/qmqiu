package com.lwt.qmqiu.network


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.HttpResult
import com.lwt.qmqiu.network.ApiConst
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {
    companion object{
        val BASE_URL_Api : String
            get() = "http://192.168.2.10:9898/api/"
        val BASE_URL : String
            get() = "http://192.168.2.10:9898/"
    }


    @POST(ApiConst.USER_REGIST)
    fun userRegist(@Query("name") name:String,@Query("password") password:String):Observable<HttpResult<BaseUser>>










   /* //查询社保卡地址：
     @GET(ApiConst.SEARCH_CARD_ADDRESS)
      fun searchCardAddress(@Query("cardname") cardname:String,@Query("cardid") cardid:String,@Query("hospitalnum") hospitalnum:String,@Query("terminalnum") terminalnum:String,@Query("cardtype") cardtype:String) :Observable<HttpResult<UserInfo>>

    //获取个人编号 地址：
     @GET(ApiConst.GET_PERSON_NUMBER)
      fun getPersonNumber(@Query("cardname") cardname:String,@Query("cardid") cardid:String,@Query("hospitalnum") hospitalnum:String,@Query("terminalnum") terminalnum:String,@Query("cardtype") cardtype:String) :Observable<HttpResult<UserInfo>>

    //上传文件测试
    @Multipart
    @POST(ApiConst.UPLOAD)
    fun testUpload( @Part("name") name: RequestBody,@Part personalimages: MultipartBody.Part):Observable<HttpResult<String>>?

    //下载测试

    //升级测试
    @GET(ApiConst.DOWNAPK_NOTICE)
    fun getUpdata(@Query("version") version:Int):Observable<HttpResult<ApkInfo>>*/


}

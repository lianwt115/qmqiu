package com.lwt.qmqiu.network


import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.HttpResult
import com.lwt.qmqiu.bean.IMChatRoom
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

    @POST(ApiConst.USER_Login)
    fun userLogin(@Query("name") name:String,@Query("password") password:String,@Query("auto") auto:Boolean,@Query("loginWhere") loginWhere:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double):Observable<HttpResult<BaseUser>>


    @GET(ApiConst.IMChat_RoomGet)
    fun getIMChatRoom(@Query("name") name:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("type") type:Int):Observable<HttpResult<List<IMChatRoom>>>

    @POST(ApiConst.IMChat_RoomCreat)
    fun creatIMChatRoom(@Query("name") name:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("type") type:Int,@Query("roomname") loginWhere:String ):Observable<HttpResult<IMChatRoom>>





}

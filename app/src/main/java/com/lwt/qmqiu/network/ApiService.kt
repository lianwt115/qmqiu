package com.lwt.qmqiu.network


import com.lwt.qmqiu.bean.*
import com.lwt.qmqiu.network.ApiConst
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    companion object{

        val IP = "192.168.2.10:9898"

        val BASE_URL_Api : String
            get() = "http://$IP/api/"
        val BASE_URL : String
            get() = "http://$IP/"
        val BASE_URL_WS : String
            get() = "ws://$IP/api/websocket/"
    }


    @POST(ApiConst.USER_REGIST)
    fun userRegist(@Query("name") name:String,@Query("password") password:String):Observable<HttpResult<BaseUser>>

    @POST(ApiConst.USER_Login)
    fun userLogin(@Query("name") name:String,@Query("password") password:String,@Query("auto") auto:Boolean,@Query("loginWhere") loginWhere:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double):Observable<HttpResult<BaseUser>>

    @POST(ApiConst.USER_LoginOut)
    fun userLoginOut(@Query("name") name:String,@Query("password") password:String,@Query("auto") auto:Boolean,@Query("loginWhere") loginWhere:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double):Observable<HttpResult<Boolean>>

    @GET(ApiConst.USER_Find)
    fun userFind(@Query("name") name:String):Observable<HttpResult<BaseUser>>

    @GET(ApiConst.IMChat_RoomGet)
    fun getIMChatRoom(@Query("name") name:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("type") type:Int):Observable<HttpResult<List<IMChatRoom>>>

    @POST(ApiConst.IMChat_RoomCreat)
    fun creatIMChatRoom(@Query("name") name:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("type") type:Int,@Query("roomname") loginWhere:String ):Observable<HttpResult<IMChatRoom>>


    @GET(ApiConst.IMChat_RoomMessageGet)
    fun getRoomMessage(@Query("name") name:String,@Query("roomNumber") roomNumber:String):Observable<HttpResult<List<QMMessage>>>

    @GET(ApiConst.IMChat_RoomActiveUser)
    fun getRoomActiveUser(@Query("name") name:String,@Query("roomNumber") roomNumber:String):Observable<HttpResult<List<BaseUser>>>

    @GET(ApiConst.IMChat_RoomExitAndDelete)
    fun getRoomExitAndDelete(@Query("name") name:String,@Query("roomNumber") roomNumber:String):Observable<HttpResult<Boolean>>

    @POST(ApiConst.Gift_Buy)
    fun giftBuy(@Query("name") name:String,@Query("cashCount") cashCount:Int,@Query("giftCount") giftCount:String,@Query("priceCount") priceCount:String):Observable<HttpResult<BaseUser>>

    @POST(ApiConst.Gift_Send)
    fun giftSend(@Query("name") name:String,@Query("to") to:String,@Query("giftIndex") giftIndex:Int,@Query("giftCount") giftCount:Int):Observable<HttpResult<BaseUser>>

    @POST(ApiConst.Refuse_User)
    fun refuseUser(@Query("from") name:String,@Query("to") to:String,@Query("refuse") refuse:Boolean):Observable<HttpResult<RefuseLog>>

    @GET(ApiConst.Refuse_Check)
    fun refuseCheck(@Query("from") name:String,@Query("to") to:String):Observable<HttpResult<Boolean>>

    @POST(ApiConst.Report_User)
    fun reportUser(@Query("from") name:String,@Query("to") to:String,@Query("why") why:Int,@Query("roomNumber") roomNumber:String,@Query("messageContent") messageContent:String,@Query("messageId") messageId:Long):Observable<HttpResult<Boolean>>

    @Multipart
    @POST(ApiConst.Upload)
    fun upload(@Query("from") name:String,@Query("type") type:Int,@Query("where") where:String,@Query("length") length:Int,@Part file:MultipartBody.Part):Observable<HttpResult<UploadLog>>

    @Streaming
    @POST(ApiConst.Download)
    fun download(@Query("id") id:String): Observable<ResponseBody>



}

package com.lwt.qmqiu.network


import com.lwt.qmqiu.bean.*
import io.reactivex.Observable
import okhttp3.MultipartBody
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

    @POST(ApiConst.USER_Updata)
    fun userUpdata(@Query("name") name: String,@Query("showname") showname: String,@Query("age") age: Int,@Query("male") male: Boolean,@Query("imgpath") imgpath: String):Observable<HttpResult<BaseUser>>

    @GET(ApiConst.IMChat_RoomGet)
    fun getIMChatRoom(@Query("name") name:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("type") type:Int):Observable<HttpResult<List<IMChatRoom>>>

    @GET(ApiConst.IMChat_RoomGetSearch)
    fun getIMChatRoomSearch(@Query("name") name:String,@Query("roomName") roomName:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("type") type:Int):Observable<HttpResult<IMChatRoom>>

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

    @POST(ApiConst.IMChat_RoomMessageVideoRequest)
    fun videoRequest(@Query("name") name:String,@Query("to") to:String,@Query("message") message:String):Observable<HttpResult<QMMessage>>

    @POST(ApiConst.IMChat_RoomMessageVideoRequestExit)
    fun videoRequestExit(@Query("channelName") channelName:String,@Query("name") name:String,@Query("time") time:Int):Observable<HttpResult<Boolean>>

    @GET(ApiConst.IMChat_RoomCreatByMe)
    fun getRoomCreatByMe(@Query("name") name:String,@Query("sys") sys:String = "sys"):Observable<HttpResult<List<IMChatRoom>>>

    @GET(ApiConst.Gift_LogGet)
    fun getGiftLog(@Query("name") name:String,@Query("type") type:Int):Observable<HttpResult<List<GiftLog>>>

    @GET(ApiConst.Coin_CoinRecord)
    fun coinRecord(@Query("name") name:String,@Query("type") type:Int,@Query("all") all:Boolean):Observable<HttpResult<List<CoinLog>>>


    @POST(ApiConst.Coin_CreatChargeNumber)
    fun creatChargeNum(@Query("name") name:String,@Query("type") type:Int,@Query("publickey") publickey:String,@Query("count") count:Int):Observable<HttpResult<String>>

    @POST(ApiConst.Coin_Charge)
    fun coinCharge(@Query("name") name:String,@Query("chargenum") chargenum:String):Observable<HttpResult<BaseUser>>

    @POST(ApiConst.Coin_Exchange)
    fun coinExchange(@Query("name") name:String,@Query("giftIndex") giftIndex:String):Observable<HttpResult<BaseUser>>

    @POST(ApiConst.Note_Get)
    fun noteGet(@Query("name") name:String,@Query("noteType") noteType:Int,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double):Observable<HttpResult<List<NoteLog>>>

    @POST(ApiConst.Note_Create)
    fun noteCreate(@Query("name") name:String,@Query("noteType") noteType:Int,@Query("seeType") seeType:Int,@Query("topic") topic:String,@Query("textContent") textContent:String,@Query("imgList") imgList:String,@Query("latitude") latitude:Double,@Query("longitude") longitude:Double,@Query("where") where:String):Observable<HttpResult<Boolean>>

    @POST(ApiConst.Note_Report)
    fun noteReport(@Query("name") name:String,@Query("id") id:String,@Query("why") why:Int):Observable<HttpResult<Boolean>>

    @POST(ApiConst.Note_Delete)
    fun noteDelete(@Query("name") name:String,@Query("id") id:String):Observable<HttpResult<Boolean>>

    @POST(ApiConst.Note_Good)
    fun noteGood(@Query("name") name:String,@Query("id") id:String):Observable<HttpResult<Boolean>>

}

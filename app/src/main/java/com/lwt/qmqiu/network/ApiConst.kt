package com.lwt.qmqiu.network


class ApiConst {
    companion object{

        const val USER_REGIST = "user/regist"
        const val USER_Login = "user/login"
        const val USER_LoginOut = "user/loginout"
        const val USER_Find = "user/findbyname"

        const val IMChat_RoomGet = "chat/getroom"
        const val IMChat_RoomCreat = "chat/creatroom"

        const val IMChat_RoomMessageGet = "message/getmessage"
        const val IMChat_RoomMessageVideoRequest = "message/sendVideoRequest"
        const val IMChat_RoomMessageVideoRequestExit = "message/exitVideoRequest"


        const val IMChat_RoomActiveUser = "enterlog/getactiveuser"
        const val IMChat_RoomExitAndDelete = "enterlog/exitanddelete"


        const val Gift_Buy = "gift/giftbuy"
        const val Gift_Send = "gift/giftsend"

        const val Refuse_User = "refuse/refuseuser"
        const val Refuse_Check = "refuse/refusecheck"

        const val Report_User = "report/reportuser"

        //上传
        const val Upload = "upload/uploadfile"

        const val Download = "upload/download"




    }
}
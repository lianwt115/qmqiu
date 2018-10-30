package com.lwt.qmqiu.network


class ApiConst {
    companion object{

        const val USER_REGIST = "user/regist"
        const val USER_Login = "user/login"
        const val USER_LoginOut = "user/loginout"

        const val IMChat_RoomGet = "chat/getroom"
        const val IMChat_RoomCreat = "chat/creatroom"

        const val IMChat_RoomMessageGet = "message/getmessage"


        const val IMChat_RoomActiveUser = "enterlog/getactiveuser"
        const val IMChat_RoomExitAndDelete = "enterlog/exitanddelete"


    }
}
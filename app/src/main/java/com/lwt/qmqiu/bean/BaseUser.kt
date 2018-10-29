package com.lwt.qmqiu.bean



data class BaseUser(
        var _id:String,
        var name:String,
        var password:String,
        var privateKey: String,
        var publicKey: String,
        var lastLoginTime:Long,
        var lastLoginOutTime:Long,
        var creatTime:Long,
        var status: Boolean?=true,
        var imgPath:String? ="***",
        var colorIndex:Int? = 10

)
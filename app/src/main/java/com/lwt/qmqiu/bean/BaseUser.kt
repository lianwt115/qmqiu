package com.lwt.qmqiu.bean



data class BaseUser(
        var _id:String,
        var name:String,
        var password:String,
        var privateKey: String,
        var publicKey: String,
        var age:Int,
        var male:Boolean
)
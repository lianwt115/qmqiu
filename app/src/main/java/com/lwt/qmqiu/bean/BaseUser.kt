package com.lwt.qmqiu.bean



data class BaseUser(
        var _id:String?=null,
        var name:String? ="lwt",
        var password:String? ="***",
        var privateKey: String?="",
        var publicKey: String?="",
        var age:Int= 18,
        var male:Boolean?=true
)
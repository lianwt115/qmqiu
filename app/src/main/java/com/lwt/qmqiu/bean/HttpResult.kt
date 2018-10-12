package com.lwt.qmqiu.bean


data class HttpResult<T> (var code:Int,var message:String,var data:T){
}
package com.lwt.qmqiu.mvp.base


interface BaseView<in T> {
    fun err(code:Int,errMessage:String?,type:Int)
}
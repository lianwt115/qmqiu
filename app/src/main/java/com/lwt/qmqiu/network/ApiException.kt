package com.lwt.qmqiu.network



class ApiException(code:Int,message: String?) : RuntimeException(message) {

    private var resultCode: Int?=null
    init {
        resultCode=code
    }

    fun getResultCode(): Int? {
        return resultCode!!
    }

}
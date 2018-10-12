package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.network.RetrofitClient



open class BaseModel(val context: Context) {

    val API_VERSION = 2

    val retrofitClient = RetrofitClient.getInstance(context)
    val apiService = retrofitClient.create(ApiService::class.java)

}
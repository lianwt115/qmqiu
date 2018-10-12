package com.lwt.qmqiu.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R


object NetworkUtils{

    /**
     * 检查当前网络是否可用
     * @return 是否连接到网络
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = App.instanceApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isConnected) {
                if (info.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    fun isNetworkErrThenShowMsg(): Boolean {
        if (!isNetworkAvailable()) {
            UiUtils.showToast( App.instanceApp().getString(R.string.net_error))

            return true
        }
        return false
    }

}
package com.lwt.qmqiu

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Environment
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import android.os.Environment.getExternalStorageDirectory
import android.support.multidex.MultiDex
import android.text.TextUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import java.io.IOException
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum


class App : Application() {

    companion object {
        private var instance: App? = null
        fun instanceApp() = instance!!

    }


    init {

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLog()

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)

        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options())

    }




    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("qmqiu")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {

                return BuildConfig.DEBUG
            }
        })
    }


    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private fun loginInfo(): LoginInfo? {
        return null
    }

    private fun options(): SDKOptions? {

        return null
    }



}
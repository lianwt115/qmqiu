package com.lwt.qmqiu.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.widget.Toast
import com.lwt.qmqiu.IMyAidlInterface
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class LocalService: Service() {

    private lateinit var binder: MyBinder
    private lateinit var conn: MyConn
    override fun onBind(intent: Intent?): IBinder? {

        return binder
    }

    override fun onCreate() {
        super.onCreate()
        binder = MyBinder()
        conn = MyConn()
    }

    internal inner class MyBinder : IMyAidlInterface.Stub() {
        override fun currentTime(): Long {
            return System.currentTimeMillis()
        }

    }

    internal inner class MyConn : ServiceConnection {
        private var iMyAidlInterface: IMyAidlInterface? = null
        private var mRebootTime = 10 * 1000
        override fun onServiceConnected(name: ComponentName, service: IBinder) {

            Logger.e("LocalService:已连接远程服务")
            //远程服务接口对象
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)

        }

        override fun onServiceDisconnected(name: ComponentName) {
            Logger.e("LocalService:远程服务已销毁")
            //开启远程服务
            this@LocalService.startService(Intent(this@LocalService, RomoteService::class.java))
            //绑定远程服务
            this@LocalService.bindService(Intent(this@LocalService, RomoteService::class.java), conn, Context.BIND_IMPORTANT)
        }
    }


    //服务启动
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Logger.e("本地服务已启动")
        this.bindService(Intent(this@LocalService, RomoteService::class.java), conn, Context.BIND_IMPORTANT)
        doSomething()
        return START_STICKY
    }

    private fun doSomething() {
        Observable.interval(3,TimeUnit.SECONDS).applySchedulers().subscribe({
            Logger.e("3s一次")
        },{
            Logger.e("服务异常")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.e("本地服务已销毁")
        //开启远程服务
        this@LocalService.startService(Intent(this@LocalService, RomoteService::class.java))
        //绑定远程服务
        this@LocalService.bindService(Intent(this@LocalService, RomoteService::class.java), conn, Context.BIND_IMPORTANT)

    }


}
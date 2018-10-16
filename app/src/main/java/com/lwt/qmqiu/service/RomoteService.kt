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
import com.lwt.qmqiu.utils.UiUtils
import com.orhanobut.logger.Logger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RomoteService: Service() {

    private lateinit var binder: MyBinder
    private lateinit var conn: MyConn
    private lateinit var singleThreadPool: ExecutorService
    override fun onBind(intent: Intent?): IBinder? {

        return binder
    }

    override fun onCreate() {
        super.onCreate()
        binder = MyBinder()
        conn = MyConn()
        singleThreadPool = Executors.newSingleThreadExecutor()
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
            Logger.e("RomoteService:已连接本地服务")
            //本地服务接口对象
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Logger.e("LocalService:本地服务已销毁")
            singleThreadPool.execute {

            }
        }
    }

    private var runnable = object :Runnable{

        override fun run() {
            try {
                //Log.e("mRebootTime","请等待:"+mRebootTime);
                //延时重启
                //Thread.sleep(1000 * 10);
                Thread.sleep(1000*10)

                val baseContext = application.baseContext

                val i = baseContext.packageManager
                        .getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                baseContext.startActivity(i)

                // TODO 应用重启后自杀，再由应用创建新进程
                Thread.sleep(100)
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(0)

            } catch (e: Exception) {
                Toast.makeText(this@RomoteService, "异常", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }

    }
    
    //服务启动
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Logger.e("远程服务已启动")
        this.bindService(Intent(this@RomoteService, RomoteService::class.java), conn, Context.BIND_IMPORTANT)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.e("远程服务已销毁")
        //开启远程服务
        this@RomoteService.startService(Intent(this@RomoteService, RomoteService::class.java))
        //绑定远程服务
        this@RomoteService.bindService(Intent(this@RomoteService, RomoteService::class.java), conn, Context.BIND_IMPORTANT)

    }




}
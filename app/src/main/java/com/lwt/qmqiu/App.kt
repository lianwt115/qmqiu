package com.lwt.qmqiu

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Environment
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import androidx.multidex.MultiDex
import android.text.TextUtils
import android.util.Base64
import com.tencent.bugly.Bugly
import com.baidu.location.BDLocation
import com.bumptech.glide.Glide
import com.guoxiaoxing.phoenix.picker.Phoenix
import com.lwt.qmqiu.activity.BaseActivity
import com.lwt.qmqiu.activity.MainActivity
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.greendao.DaoMaster
import com.lwt.qmqiu.greendao.DaoSession
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.StaticValues
import com.tencent.bugly.beta.Beta


class App : Application() {

    private lateinit var mDaoSession: DaoSession
    private val APP_ID = "8bfb98c056" // TODO 替换成bugly上注册的appid
    private val NOTIFICATION = "notification"
    private val DB_NAME = "qmiqu"
    private val LOG_NAME = "qmiqulog"
    private var mLocalUser:BaseUser?= null
    private var mBDLocation: BDLocation?= null
    private var mBDLocationString: String = "未知地区"
    private  var mWebSocket: QMWebsocket?=null
    private  var mCurrentActivity: BaseActivity?=null
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
        SDKInitializer.setCoordType(CoordType.GCJ02)

        initBuglyAndUP()

        if (applicationContext.packageName == (getCurrentProcessName())) {

           // Logger.e("initWebsocket")

            initWebsocket()

            initDb()

            initPhonex()


        }

    }

    private fun initPhonex() {

        Phoenix.config()
                .imageLoader { context, imageView, imagePath, type ->
                    Glide.with(context)
                            .load(imagePath)
                            .into(imageView)
                }
    }

    private fun initWebsocket() {

        if (!TextUtils.isEmpty(StaticValues.mLocalUserName)){

            mWebSocket = QMWebsocket()

            mWebSocket!!.connect(NOTIFICATION.plus(StaticValues.mLocalUserName),null)
        }


    }

    private fun initDb(){

        // regular SQLite database
        val helper = DaoMaster.DevOpenHelper(instance, DB_NAME)
        val db = helper.writableDb

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        mDaoSession = DaoMaster(db).newSession()
    }

    fun getDaoSession(): DaoSession {
        return mDaoSession
    }

    fun closeWs(){

        mWebSocket?.close()
    }

    fun setListen(listen: QMWebsocket.QMMessageListen?){
        this.mWebSocket?.setListen(listen)
    }

    private fun  initBuglyAndUP(){
        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 3*60 * 1000

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.logo

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.logo

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.logo

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity::class.java)

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(applicationContext, APP_ID, BuildConfig.DEBUG)
    }


    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(LOG_NAME)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {

                return true
            }
        })
    }




    fun setLocalUser(baseUser: BaseUser){

        SPHelper.getInstance().put("loginName",baseUser.name)
        SPHelper.getInstance().put("loginPassword",baseUser.password)

        StaticValues.updataLocalUserName()

        this.mLocalUser = baseUser

        if (mWebSocket == null)
            initWebsocket()

    }
    fun updataLocalUser(baseUser: BaseUser,gift:Boolean=false,coin:Boolean=false,base:Boolean =false){

        if (gift)
            this.mLocalUser?.gift = baseUser.gift

        if (coin){

            this.mLocalUser?.coin = baseUser.coin
            this.mLocalUser?.coinbase = baseUser.coinbase
            this.mLocalUser?.coinExchange = baseUser.coinExchange
        }

        if (base){

            this.mLocalUser?.showName = baseUser.showName
            this.mLocalUser?.age = baseUser.age
            this.mLocalUser?.male = baseUser.male
            this.mLocalUser?.imgPath = baseUser.imgPath

        }

    }

    fun getLocalUser():BaseUser?{

        return this.mLocalUser

    }

    fun userExit(){

        SPHelper.getInstance().put("loginName","")
        SPHelper.getInstance().put("loginPassword","")

        StaticValues.updataLocalUserName()
        closeWs()

        this.mLocalUser = null
    }
    fun isLogin():Boolean{

        return this.mLocalUser != null

    }
    fun getBDLocation():BDLocation?{

        return this.mBDLocation

    }
    fun setBDLocation(location:BDLocation?) {

        this.mBDLocation = location

    }

    fun getBDLocationString(): String {

        return this.mBDLocationString

    }

    fun setBDLocationString(locationString: String) {

        this.mBDLocationString = locationString

    }

    /**
  * 获取当前进程名
     */
    private fun getCurrentProcessName():String {
        var pid = android.os.Process.myPid()
        var processName = ""
        var manager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        manager.runningAppProcesses.forEach {

            if (it.pid == pid) {
                processName = it.processName
            }
        }

        return processName
    }


     fun getShowMessage(message: String):String{

        if (this.mLocalUser != null){

            try{

                return String(RSAUtils.decryptData(Base64.decode(message,0), RSAUtils.loadPrivateKey(this.mLocalUser!!.privateKey))!!)


            }catch (e:Exception){


                return message

            }


        }else{

            return message
        }

    }

    fun setCurrentActivity(activity: BaseActivity?){

        this.mCurrentActivity = activity

    }

    fun notificationActivity(qmMessage: QMMessage){

        if (this.mCurrentActivity!=null)
            this.mCurrentActivity!!.qmMessage(qmMessage)
    }

}
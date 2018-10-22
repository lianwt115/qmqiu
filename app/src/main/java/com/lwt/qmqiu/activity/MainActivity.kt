package com.lwt.qmqiu.activity

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import android.text.TextUtils
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.lwt.qmqiu.App
import com.lwt.qmqiu.BuildConfig
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.UserLoginContract
import com.lwt.qmqiu.mvp.present.UserLoginPresent
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.widget.MapNoticeDialog
import com.tencent.bugly.beta.Beta



class MainActivity : BaseActivity(), View.OnClickListener, MapNoticeDialog.MapNoticeDialogListen, MapLocationUtils.FindMeListen, UserLoginContract.View {

    private lateinit var mBaiduMap:BaiduMap
    private lateinit var mMapNoticeDialog:MapNoticeDialog
    private lateinit var present:UserLoginPresent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequirePermissions()

        initView()

        //需要初始化
        Beta.init(applicationContext, BuildConfig.DEBUG)

        //startService(Intent(this, LocalService::class.java))
        //startService(Intent(this, RomoteService::class.java))

        present = UserLoginPresent(this,this)

        MapLocationUtils.getInstance().findMe(this)

    }

    private fun initView() {

        location_bt.setOnClickListener(this)
        fab.setOnClickListener(this)

        mBaiduMap = bmapView.map
    }
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.location_bt -> {

                MapLocationUtils.getInstance().findMe(this)

            }
            R.id.fab  -> {
                window.exitTransition = null
                window.enterTransition = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName())
                    startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
                } else {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    //权限检查
    fun checkAndRequirePermissions(){

        //判断系统版本是否是6.0以上

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            var per: RxPermissions?= RxPermissions(this)
            per?.request( Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ?.subscribe {
                        if (it) {

                            Logger.e("权限全部同意")

                        } else {
                            //至少一个没有同意
                            showProgressDialog("权限被拒绝,稍后将退出")

                            Observable.timer(3, TimeUnit.SECONDS).applySchedulers().subscribe({

                                finish()

                            }, {

                                Logger.e(it.message)

                            })
                        }
                    }
        }else{

            Logger.e("系统版本低于6.0,无需动态申请权限")

        }

    }



    override fun onDestroy() {

        super.onDestroy()
        bmapView.onDestroy()

    }

    override fun onPause() {
        super.onPause()
        bmapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        bmapView.onResume()

    }

    override fun onStop() {
        super.onStop()
        MapLocationUtils.getInstance().exit()
    }

    //将定位瞄在地图上
    fun locationOnMap(location: BDLocation){
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true

        // 构造定位数据
        val locData = MyLocationData.Builder()
                .accuracy(location.radius)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100f).latitude(location.latitude)
                .longitude(location.longitude).build()

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData)

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        val mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.logo)

        val config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null, 0xAAFFFF88.toInt(), 0xAA00FF00.toInt())
        mBaiduMap.setMyLocationConfiguration(config)


        // 当不需要定位图层时关闭定位图层
        //mBaiduMap.isMyLocationEnabled = false

        moveToLocation(location)
    }

    //移动中心点到当前位置
    fun moveToLocation(location: BDLocation){

        val ll =  LatLng(location.latitude, location.longitude)
        val builder =  MapStatus.Builder()
        builder.target(ll).zoom(18.0f)
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))


        val build =MapNoticeDialog.Builder(this,location)

        mMapNoticeDialog = build.create(this)

        mMapNoticeDialog.show()

    }

    override fun clickState(status: Boolean, location: BDLocation, mapNoticeDialog: MapNoticeDialog) {

        if (status){

            val intent = Intent(this, FaceVideoActivity::class.java)

            intent.putExtra("location",location)

            startActivity(intent)

        }

        mapNoticeDialog.dismiss()
    }

    override fun locationInfo(location: BDLocation?) {
        locationOnMap(location!!)
        autoLogin(location!!)
    }


    override fun successRegistOrLogin(baseUser: BaseUser, regist: Boolean) {

        Logger.e("自动登录成功")
        App.instanceApp().setLocalUser(baseUser)

        QMWebsocket.getInstance().connect(baseUser)

    }

    override fun err(code: Int, errMessage: String?) {

        Logger.e("自动登录失败:code:$code ** errMessage:$errMessage")

    }

    private fun autoLogin(location: BDLocation) {
        //是否已经登录

        if (App.instanceApp().getLocalUser() == null) {

            val name = SPHelper.getInstance().get("loginName","") as String
            val password = SPHelper.getInstance().get("loginPassword","") as String

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password))

                present.userLogin(name,password,true,location.addrStr,location.latitude,location.longitude,bindToLifecycle())

        }

    }


}

package com.lwt.qmqiu.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.lwt.qmqiu.R
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import com.baidu.location.LocationClientOption.LocationMode
import com.baidu.location.LocationClientOption
import android.icu.util.ULocale.getCountry
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.core.RouteNode.location
import com.baidu.mapapi.model.LatLng
import com.lwt.qmqiu.BuildConfig
import com.lwt.qmqiu.im.IMUtils
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.utils.newIntent
import com.lwt.qmqiu.widget.MapNoticeDialog
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport


class MainActivity : BaseActivity(), View.OnClickListener, MapNoticeDialog.MapNoticeDialogListen, MapLocationUtils.FindMeListen {


    private lateinit var mBaiduMap:BaiduMap
    private lateinit var mMapNoticeDialog:MapNoticeDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequirePermissions()

        initView()

        //需要初始化
        Beta.init(applicationContext, BuildConfig.DEBUG)
    }

    private fun initView() {

        location_bt.setOnClickListener(this)

        mBaiduMap = bmapView.map
    }
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.location_bt -> {

                MapLocationUtils.getInstance().findMe(this)

            }
            else -> {
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
        MapLocationUtils.getInstance().findMe(this)
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
    }


}

package com.lwt.qmqiu.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.ClipboardManager
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.LocationInfo
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_mapinfo.*
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.utils.CoordinateConverter
import com.baidu.mapsdkplatform.comapi.location.CoordinateType.BD09LL
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.ReporterDialog


/**
 * 位置信息点击
 */
class MapInfoActivity : BaseActivity(), BarView.BarOnClickListener, View.OnClickListener{

    private lateinit var mReporterDialogBuilder:ReporterDialog.Builder
    private lateinit var mReporterDialog:ReporterDialog

    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()

            }

        }
    }

    private lateinit var mBaiduMap:BaiduMap

    private lateinit var mLocationInfo:LocationInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapinfo)

        mLocationInfo = intent.getParcelableExtra("locationinfo")

        initView()

        locationOnMap(mLocationInfo.latitude,mLocationInfo.longitude)
        //地图覆盖物
        addMarker(mLocationInfo.latitude,mLocationInfo.longitude)
    }


    private fun showLocation(location: BDLocation?) {

        if (location==null)
            return

        locationOnMap(location.latitude,location.longitude)


    }

    private fun initView() {

        map_barview.changeTitle("位置信息")

        map_barview.setBarOnClickListener(this)

        map_barview.showMore(false)
        //不显示缩放控制
        bmapView.showZoomControls(false)

        mBaiduMap = bmapView.map

        location_img.setOnClickListener(this)
        select_img.setOnClickListener(this)

        location_info.text = mLocationInfo.locationName.plus("(${mLocationInfo.locationWhere})")
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.location_img -> {

                showLocation(App.instanceApp().getBDLocation())

            }

            R.id.select_img -> {


                showSelectDialog()

            }


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
    }

    //将定位瞄在地图上
    private fun locationOnMap(latitude:Double,longitude:Double){
        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true

        // 构造定位数据
        val locData = MyLocationData.Builder()
                .accuracy(10.0f)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100f).latitude(latitude)
                .longitude(longitude).build()

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData)

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        val mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.logo)

        val config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null, 0x33FF4081.toInt(), 0x33FF4081.toInt())

        mBaiduMap.setMyLocationConfiguration(config)

        // 当不需要定位图层时关闭定位图层
        //mBaiduMap.isMyLocationEnabled = false

        moveToLocation(latitude,longitude)
    }

    //移动中心点到当前位置
    private fun moveToLocation(latitude: Double, longitude: Double){

        val ll =  LatLng(latitude,longitude)
        val builder =  MapStatus.Builder()
        builder.target(ll).zoom(18.0f)
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

    }

    private fun addMarker(latitude: Double, longitude: Double){

        val point = LatLng(latitude,longitude)

        //构建Marker图标

        val bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.maker)

        //构建MarkerOption，用于在地图上添加Marker

        val option = MarkerOptions()
                .position(point)
                .icon(bitmap)

        //在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option)

    }

    private fun showSelectDialog(){
        mReporterDialogBuilder =  ReporterDialog.Builder(this,true)

        mReporterDialog = mReporterDialogBuilder.create("选择打开地图",object : ReporterDialog.Builder.BtClickListen{

            override fun btClick(index: Int, type: Int): Boolean {

                when (type) {
                    //选择
                    2 -> {

                        when (index) {

                            0 -> {

                                if (checkApkExist("com.baidu.BaiduMap")) {

                                    val i1 = Intent()
                                    // 地址解析
                                    i1.data = Uri.parse("baidumap://map/geocoder?src=com.lwt.qmqiu&address=${mLocationInfo.locationWhere}${mLocationInfo.locationName}")

                                    startActivity(i1)
                                }else{

                                    showProgressDialog("没有检测到百度地图APP")
                                }


                            }

                            1 -> {

                                if (checkApkExist("com.autonavi.minimap")) {

                                    //dat=androidamap://viewMap?sourceApplication=appname&poiname=abc&lat=36.2&lon=116.1&dev=0

                                    val i1 = Intent()
                                    // 地址解析
                                    i1.data = Uri.parse("androidamap://viewMap?sourceApplication=com.lwt.qmqiu&poiname=${mLocationInfo.locationWhere}${mLocationInfo.locationName}&lat=${mLocationInfo.latitude}&lon=${mLocationInfo.longitude}&dev=0")

                                    startActivity(i1)
                                }else{

                                    showProgressDialog("没有检测到高德地图APP")

                                }


                            }

                            2 -> {

                                if (checkApkExist("com.tencent.map")) {

                                    val i1 = Intent()
                                    // 地址解析
                                    i1.data = Uri.parse("qqmap://map/geocoder?coord=${mLocationInfo.latitude},${mLocationInfo.longitude}&referer=K4CBZ-I2P3U-ZOIVZ-BBBUQ-DTJ36-2GFR2")

                                    startActivity(i1)
                                }else{
                                    showProgressDialog("没有检测到腾讯地图APP")
                                }


                            }
                        }

                    }

                }

                mReporterDialog.dismiss()

                return false
            }

        },2)

        mReporterDialog.show()
    }


    private fun checkApkExist(packageName:String?):Boolean{


        if (packageName == null || "" == packageName)

            return false
        return try {

            var info = packageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)

            true
        } catch ( e:PackageManager.NameNotFoundException) {

            false
        }


    }


}

package com.lwt.qmqiu.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.lwt.qmqiu.App
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_map.*
import com.baidu.location.Poi
import com.lwt.qmqiu.R.mipmap.location
import com.orhanobut.logger.Logger


class MapActivity : BaseActivity(), BarView.BarOnClickListener, View.OnClickListener {



    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()

            }

            false -> {

                val i1 = Intent()
                   // 地址解析
                i1.data = Uri.parse("baidumap://map/geocoder?src=com.lwt.qmqiu&address=${App.instanceApp().getBDLocationString()}")

                startActivity(i1)
            }
        }
    }

    private lateinit var mBaiduMap:BaiduMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initView()

        showLocation()
    }

    private fun showLocation() {
        var location = App.instanceApp().getBDLocation()

        locationOnMap(location!!)

        val poiList = location.poiList

        for (poi in poiList) {

            Logger.e("${poi.name} -- ${poi.id} --${poi.rank}")
        }
    }

    private fun initView() {

        map_barview.changeTitle("位置")

        map_barview.setBarOnClickListener(this)

        //不显示缩放控制
        bmapView.showZoomControls(false)

        mBaiduMap = bmapView.map


        location_img.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.location_img -> {

                showLocation()

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

        val config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null, 0x33FF4081.toInt(), 0x33FF4081.toInt())

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

    }

}

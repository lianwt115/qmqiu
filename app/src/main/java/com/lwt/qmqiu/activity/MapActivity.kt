package com.lwt.qmqiu.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.lwt.qmqiu.App
import com.lwt.qmqiu.adapter.LocationListAdapter
import com.lwt.qmqiu.bean.LocationInfo
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : BaseActivity(), BarView.BarOnClickListener, View.OnClickListener, LocationListAdapter.TextClickListen{


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()

            }

            false -> {

                val intent = Intent()
                //把返回数据存入Intent
                intent.putExtra("locationinfo", mLocationInfo)
                //设置返回数据
                this@MapActivity.setResult(Activity.RESULT_OK, intent)

                finish()
            }
        }
    }

    private lateinit var mBaiduMap:BaiduMap
    private lateinit var mLocationListAdapter:LocationListAdapter
    private lateinit var mLocationInfo:LocationInfo
    private var mLocationList = ArrayList<LocationInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initView()

        initRecycleView()

        showLocation(App.instanceApp().getBDLocation())

    }

    private fun initRecycleView() {


        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        recycleview_location.layoutManager=linearLayoutManager

        mLocationListAdapter= LocationListAdapter(this,mLocationList,this)

        recycleview_location.adapter = mLocationListAdapter

        recycleview_location.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })

    }

    override fun textClick(content: LocationInfo, position: Int) {

        this.mLocationInfo = content

    }

    private fun showLocation(location: BDLocation?) {

        if (location==null)
            return

        locationOnMap(location.latitude,location.longitude)

        val poiList = location.poiList

        mLocationList.clear()


        poiList.forEachIndexed { index, poi ->

            mLocationList.add(LocationInfo(poi.name,"${if (index == 0) "" else location.province}${location.city}${location.district}${location.street}${location.streetNumber}",location.latitude,location.longitude,index == 0))

        }
        mLocationInfo = mLocationList[0]

        mLocationListAdapter.notifyDataSetChanged()

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

                showLocation(App.instanceApp().getBDLocation())

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
    fun locationOnMap(latitude:Double,longitude:Double){
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
    fun moveToLocation(latitude: Double, longitude: Double){

        val ll =  LatLng(latitude,longitude)
        val builder =  MapStatus.Builder()
        builder.target(ll).zoom(18.0f)
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

    }

}

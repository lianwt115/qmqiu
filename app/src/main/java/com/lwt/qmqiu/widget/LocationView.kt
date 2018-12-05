package com.lwt.qmqiu.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.LocationInfo


/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class LocationView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr), View.OnClickListener {


    private lateinit var mTextViewTitle: TextView

    private lateinit var mMapView: TextureMapView
    private lateinit var mMapViewRoot: FrameLayout
    private lateinit var mBaiduMap: BaiduMap

    private var mBarOnClickListener: BarOnClickListener? = null

    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {

        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    fun setBarOnClickListener(mBarOnClickListener: BarOnClickListener) {
        this.mBarOnClickListener = mBarOnClickListener
    }

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_locationview, null)

        //输入标题
        mTextViewTitle = view.findViewById(R.id.title_map) as TextView
        mMapView = view.findViewById(R.id.bmapView) as TextureMapView
        mMapViewRoot = view.findViewById(R.id.mapview_root) as FrameLayout

        //地图设置
        mBaiduMap = mMapView.map
        //不显示缩放控制
        mMapView.showZoomControls(false)

        mMapViewRoot.setOnClickListener(this)

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }
    //将定位瞄在地图上
    fun locationOnMap(locationInfo: LocationInfo){


        // 开启定位图层
        mBaiduMap.isMyLocationEnabled = true

        // 构造定位数据
        val locData = MyLocationData.Builder()
                .accuracy(10.0f)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100f).latitude(locationInfo.latitude)
                .longitude(locationInfo.longitude).build()

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData)

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        val mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.mipmap.logo)

        val config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null, 0x33FF4081.toInt(), 0x33FF4081.toInt())

        mBaiduMap.setMyLocationConfiguration(config)


        // 当不需要定位图层时关闭定位图层
        //mBaiduMap.isMyLocationEnabled = false

        moveToLocation(locationInfo)

        mTextViewTitle.text = locationInfo.locationName.plus("(${locationInfo.locationWhere})")
    }

    //移动中心点到当前位置
    private fun moveToLocation(locationInfo: LocationInfo){

        val ll =  LatLng(locationInfo.latitude,locationInfo.longitude)
        val builder =  MapStatus.Builder()
        builder.target(ll).zoom(18.0f)
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

    }

    override fun onClick(v: View?) {

        if (mBarOnClickListener==null) {
            return
        }

        when (v?.id) {


            R.id.mapview_root  -> {

                mBarOnClickListener!!.barViewClick()

            }

        }

    }



    interface BarOnClickListener {

        fun barViewClick()

    }

}
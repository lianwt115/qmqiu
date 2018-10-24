package com.lwt.qmqiu.map

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.lwt.qmqiu.App
import com.orhanobut.logger.Logger

class MapLocationUtils private constructor() {


        companion object {
            @Volatile
            private var mInstance: MapLocationUtils? = null

            fun getInstance(): MapLocationUtils {
                if (mInstance == null) {
                    synchronized(MapLocationUtils::class.java) {
                        if (mInstance == null) {
                            mInstance = MapLocationUtils()
                        }
                    }
                }
                return mInstance!!
            }
        }


    private  var mLocationClient:LocationClient?=null

    private  var mFindMeListen:FindMeListen?=null

    interface FindMeListen {

        fun locationInfo(location: BDLocation?)

    }

    fun  findMe(findMeListen: FindMeListen?){

        this.mFindMeListen = findMeListen

        if (mLocationClient == null)
            initLocation()


        mLocationClient!!.start()

    }

    fun exit(){

        mFindMeListen = null

    }

    private fun initLocation(){

        mLocationClient = LocationClient(App.instanceApp())

        mLocationClient!!.registerLocationListener(mLocationListen)

        val option = LocationClientOption()

        option.setIsNeedLocationDescribe(true)
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true


        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll")
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(0)
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.isOpenGps = true
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.isLocationNotify = true
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false)
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false)
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000)
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false)
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient!!.locOption = option
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

    }



    private var mLocationListen =object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            App.instanceApp().setBDLocation(location)

            val latitude = location?.latitude    //获取纬度信息
            val longitude = location?.longitude    //获取经度信息
            val radius = location?.radius    //获取定位精度，默认值为0.0f

            val coorType = location?.coorType
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            /* 返回值	返回值说明
                     61	GPS定位结果，GPS定位成功
                     62	无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位
                     63	网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位
                     66	离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
                     67	离线定位失败
                     161	网络定位结果，网络定位成功
                     162	请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件
                     167	服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位
                     505	AK不存在或者非法，请按照说明文档重新申请AK*/
            val errorCode = location?.locType
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            //Logger.e("定位成功:".plus(errorCode==161))

            //Logger.e("定位类型:$coorType**纬度:$latitude**经度:$longitude**定位精度:$radius**错误码:$errorCode**")

            val addr = location?.addrStr    //获取详细地址信息
            val country = location?.country    //获取国家
            val province = location?.province    //获取省份
            val city = location?.city    //获取城市
            val district = location?.district    //获取区县
            val street = location?.street    //获取街道信息

           /* Logger.e("详细地址:$addr" +
                    "**国家:$country" +
                    "**省份:$province" +
                    "**城市:$city" +
                    "**区县:$district" +
                    "**街道:$street"
            )*/
            val locationDescribe = location?.locationDescribe    //获取位置描述信息
            //Logger.e("周边描述信息:$locationDescribe")

            if (mFindMeListen != null)
                mFindMeListen?.locationInfo(location)

            mLocationClient!!.stop()

        }

    }

    }

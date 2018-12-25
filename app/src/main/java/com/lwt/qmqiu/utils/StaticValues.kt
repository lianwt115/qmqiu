package com.lwt.qmqiu.utils

import com.lwt.qmqiu.App
import com.lwt.qmqiu.R

class StaticValues {

    companion object {

         val giftUnitList = listOf("个","朵","台","顶")
         val giftNameList = listOf("天使宝贝","挚爱玫瑰","激情跑车","女王皇冠")
         val giftPathList = listOf("angel.svga","rose.svga","posche.svga","kingset.svga")
         val mLocalUserName:String = SPHelper.getInstance().get("loginName","") as String
         val mLocalUserPassword:String = SPHelper.getInstance().get("loginPassword","") as String
         val AdminName:String = "lwt520"
         val AdminIMEI:String = "862400046671037"
         val AdminPHONE:String = "+8613264736041"
         val app= App.instanceApp()
         var mNoteTabs = listOf<String>(app.getString(R.string.list4_name), app.getString(R.string.list5_name),
                app.getString(R.string.list6_name)).toMutableList()

    }
}
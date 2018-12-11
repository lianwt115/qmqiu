package com.lwt.qmqiu.utils

class StaticValues {

    companion object {

         val giftUnitList = listOf("个","朵","台","顶")
         val giftNameList = listOf("天使宝贝","挚爱玫瑰","激情跑车","女王皇冠")
         val giftPathList = listOf("angel.svga","rose.svga","posche.svga","kingset.svga")
         val mLocalUserName:String = SPHelper.getInstance().get("loginName","") as String
         val AdminName:String = "lwt520"
         val AdminIMEI:String = "862400046671037"
         val AdminPHONE:String = "+8613264736041"

    }
}
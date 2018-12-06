package com.lwt.qmqiu.bean

import com.lwt.qmqiu.R


data class GiftInfo (var count:Int, var imgPath:Int= R.mipmap.angel, var savgPath:String= "angel.svga",var price:Int=100,var select:Boolean = false){
}
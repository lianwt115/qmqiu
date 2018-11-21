package com.lwt.qmqiu.bean

data class QMMessage(
        var from:String="",
        var to:String="",
        //0 普通消息 2 礼物 3 语音 4 图片
        var type:Int=0,
        var message:String="",
        var colorIndex:Int=0,
        var imgPath:String="",
        var currentCount:Int=0,
        var time:Long = 0) {




}
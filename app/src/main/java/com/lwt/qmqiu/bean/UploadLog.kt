package com.lwt.qmqiu.bean


data class UploadLog(

        var _id:String,
        //谁上传
        var from:String,
        //什么类型 0,语音 1 图片 2 文件
        var type:Int,
        //那个房间
        var where:String,
        //文件位置
        var path:String,
        //文件名称
        var name:String,
        //文件大小
        var length:Int,
        //上传时间
        var creatTime:Long

)
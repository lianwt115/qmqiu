package com.lwt.qmqiu.bean



data class RefuseLog(

        var _id:String?=null,
        //谁
        var from:String? = "",
        //阻止谁   即拒绝产生私人聊天
        var to:String? = "",
        //是否阻止
        var status:Boolean? = false,
        //交易时间
        var creatTime:Long = System.currentTimeMillis(),
        var changeTime:Long = System.currentTimeMillis()

)
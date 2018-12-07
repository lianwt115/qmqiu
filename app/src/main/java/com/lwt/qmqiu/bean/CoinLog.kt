package com.lwt.qmqiu.bean




data class CoinLog(

        var _id:String?=null,

        //0 青木 1青木球        10青木  -1 青木球
        var coinType:Int? = 0,
        //0 充值 1消费  充值只能充值青木球
        var cashType:Int? = 0,
        //交易额
        var cash:Int = 0,
        //主动方 或创建充值码的人
        var name:String?="",
        //如果是消费 则记录用途 0 开附近房 1开公共房 2购买礼物 3视频聊天 4开私人房
        var toType:Int?=0,

        //充值码
        var chargeNumber:String="",
        //是否已经使用
        var used:Boolean = false,
        //谁使用
        var chargeUser:String = "",
        //创建时间
        var happenTime:Long = System.currentTimeMillis(),
        //创建时间
        var chargeTime:Long = 0L

)
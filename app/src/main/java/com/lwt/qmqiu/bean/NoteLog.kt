package com.lwt.qmqiu.bean




data class NoteLog(

        var _id:String?=null,

        //谁创建
        var name:String ="lwt",
        var showName:String ="lwt",
        var nameImg:String ="lwt",
        //点赞数
        var goodNum:Int = 0,
        //帖子类型  1 日常 2. 二手 ,3.租赁
        var noteType:Int = 1,
        //可见范围 1 附近 2.所有人
        var seeType:Int = 1,
        //话题
        var topic:String = "",
        //文字内容
        var textContent:String="",
        //以LWT分割
        var imgList:String = "",
        //评论数
        var commentNum:Int = 0,
        //举报次数
        var reportNum:Int = 0,
        //帖子发表于何处
        var latitude:Double=0.00,
        var longitude:Double=0.00 ,
        var where:String="" ,
        var deleteStatus:Boolean = false,
        var creatTime:Long = System.currentTimeMillis(),
        var deleteTime:Long = 0


)
package com.lwt.qmqiu.bean


data class NoteCommentLog(

        var _id:String?=null,

        //谁
        var from:String? = "",
        var fromShow:String? = "",
        var fromImg:String? = "",
        //评论谁
        var to:String? = "",
        //1 帖子评论
        var type:Int = 0,
        //帖子id
        var whereId:String? = "",
        //评论内容
        var commentContent:String? = "",
        //评论时间
        var commentTime:Long = System.currentTimeMillis()

)
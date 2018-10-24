package com.lwt.qmqiu.bean

data class IMChatRoom(var _id:String?=null,

                      var roomName:String="",
                      var roomNumber:String="",
                      var roomType:Int= 0,
                      var creatName:String="",
                      var lastContent:String="" ,
                      var latitude:Double=0.00,
                      var longitude:Double=0.00 ,
                      var creatTime:Long=System.currentTimeMillis(),
                      var lastContentTime:Long=System.currentTimeMillis(),
                      var status:Boolean = true) {




}
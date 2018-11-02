package com.lwt.qmqiu.network

import com.google.gson.Gson
import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.WSErr
import com.lwt.qmqiu.utils.RxBus
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class QMWebsocket {



    private  var webSocket: WebSocket? = null
    private  var client: OkHttpClient = OkHttpClient()
    private  var request: Request?=null
    private  var listen: QMMessageListen? = null
    private  var connectStatus  = false
    private  var url  = "ws://192.168.2.10:9898/api/websocket/"
    private  var connectUrl  = ""
    private  var connectcount  = 0
    private  var connectcountMax  = 20*3


    fun connect(wsUrl: String,listen:QMMessageListen?): QMWebsocket {
        //将姓名使用公钥加密
        this.request =  Request.Builder()

                .url(url.plus(wsUrl))
                .build()

        client.newWebSocket(request, listener)

        this.listen = listen

        this.connectUrl = wsUrl

        return this
    }

    fun  setListen(listen:QMMessageListen?){

        this.listen = listen

    }

    private var listener = object : WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            this@QMWebsocket.webSocket = webSocket

            this@QMWebsocket.connectStatus = true

            if (this@QMWebsocket.listen != null)
                this@QMWebsocket.listen!!.errorWS(2,"连接成功")

            Logger.e("onOpen:$response")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Logger.e("onFailure:${t.message}")
            this@QMWebsocket.connectStatus = false

            if (this@QMWebsocket.listen != null)
                this@QMWebsocket.listen!!.errorWS(1,"连接失败")


            if (connectcount<connectcountMax)
                tryConnect()

        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Logger.e("onClosing:$reason")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            Logger.e("onMessagetext:$text")

            var gson = Gson()
            var qmMessage = gson.fromJson<QMMessage>(text,QMMessage::class.java)

            if (this@QMWebsocket.listen != null)

                this@QMWebsocket.listen!!.qmMessage(qmMessage)

        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Logger.e("onMessagebytes:${bytes.base64Url()}")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Logger.e("onClosed:$reason")
            if (this@QMWebsocket.listen != null)
                this@QMWebsocket.listen!!.errorWS(0,"连接已关闭")
        }
    }

    private fun tryConnect() {

        Observable.timer(3,TimeUnit.SECONDS).applySchedulers().subscribe({

            Logger.e("重连次数:$connectcount")

            client.newWebSocket(this.request!!, this@QMWebsocket.listener)

            connectcount++

        },{
            Logger.e("重连异常")
        })



    }


    fun close(){

        this.listen = null

        webSocket?.close(1000,"客户端关闭连接")

       // client.dispatcher()?.executorService()?.shutdown()
    }

    fun sengText(content: QMMessage, roomNumber: String){

        if (webSocket == null)
            return

        content.from = App.instanceApp().getLocalUser()?.name?:"xxx"

        content.colorIndex = App.instanceApp().getLocalUser()?.colorIndex?:0

        content.imgPath = App.instanceApp().getLocalUser()?.imgPath?:"qmqiuimg/nddzx.jpg"

        //到时候为房间号
        content.to = roomNumber
        //消息类型
        content.type = 0

        //将对象转为json
        var gson =Gson()

        webSocket?.send(gson.toJson(content))

    }


    fun setMessageListen(listen:QMMessageListen){

        this.listen = listen

    }

    interface QMMessageListen{

        fun qmMessage(message:QMMessage)
        fun errorWS(type:Int,message:String)
    }



}
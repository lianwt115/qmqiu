package com.lwt.qmqiu.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.utils.SPHelper
import com.orhanobut.logger.Logger
import okhttp3.*
import okio.ByteString

class QMWebsocket {


    private  var webSocket: WebSocket? = null

    companion object {
        @Volatile
        private var mInstance: QMWebsocket? = null

        fun getInstance(): QMWebsocket {
            if (mInstance == null) {
                synchronized(QMWebsocket::class.java) {
                    if (mInstance == null) {
                        mInstance = QMWebsocket()
                    }
                }
            }
            return mInstance!!
        }
    }


    fun connect(url: String){

        var request =  Request.Builder()
                //.url("ws://localhost:9898/api/websocket")
                .url(url)
                .build()
        var client = OkHttpClient()

        client?.newWebSocket(request, listener)

    }

    private var listener = object : WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            this@QMWebsocket.webSocket = webSocket
            this@QMWebsocket.sengText(QMMessage("LWT","DSB",0,"我爱你"))
            Logger.e("onOpen:$response")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Logger.e("onFailure:${t.message}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Logger.e("onClosing:$reason")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Logger.e("onMessagetext:$text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Logger.e("onMessagebytes:${bytes.base64Url()}")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Logger.e("onClosed:$reason")

        }
    }


    fun close(){

        webSocket?.close(1000,"客户端关闭连接")

    }

    fun sengText(content:QMMessage){
        //将对象转为json
        var gson =Gson()

        webSocket?.send(gson.toJson(content))

    }

}
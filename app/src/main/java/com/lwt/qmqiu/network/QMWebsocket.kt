package com.lwt.qmqiu.network

import android.util.Base64
import android.util.Base64.encodeToString
import com.google.gson.Gson
import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.utils.RSAUtils
import com.orhanobut.logger.Logger
import okhttp3.*
import okio.ByteString

class QMWebsocket {



    private  var webSocket: WebSocket? = null
    private  var client: OkHttpClient? = null
    private  var listen: QMMessageListen? = null
    private  var url  = "ws://192.168.2.10:9898/api/websocket/"

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


    fun connect(user: BaseUser){
        //将姓名使用公钥加密

        var request =  Request.Builder()

                .url(url.plus(user.name))
                .build()
        var client = OkHttpClient()

        client?.newWebSocket(request, listener)

        

    }

    private var listener = object : WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            this@QMWebsocket.webSocket = webSocket
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

        }
    }


    fun close(){

        this.listen = null
        webSocket?.close(1000,"客户端关闭连接")
        //client?.dispatcher()?.executorService()?.shutdown()
    }

    fun sengText(content:QMMessage){


        content.from = App.instanceApp().getLocalUser()!!.name

        //到时候为房间号
        content.to = "public"
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
    }



}
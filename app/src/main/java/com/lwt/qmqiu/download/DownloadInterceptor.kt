package com.lwt.qmqiu.download


import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response

class DownloadInterceptor(downloadListener:DownloadListen): Interceptor {

    private var downloadListener:DownloadListen =downloadListener

    var fileName = ""

    override fun  intercept(chain: Interceptor.Chain?): Response {

        var response = chain?.proceed(chain.request())


        var fileName= response?.header("Content-Disposition")!!.split("filename=")[1]

        Logger.e(fileName.substring(1,fileName.length-1))

        this.fileName = fileName.substring(1,fileName.length-1)

        return response?.newBuilder()?.body(
                 DownloadResponseBody(response.body()!!, downloadListener))!!.build()
    }

}
package com.lwt.qmqiu.download


import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.io.InputStream


class DownloadResultFunc : Function<ResponseBody, InputStream> {

            @Throws(Exception::class)
            override fun apply(responseBody: ResponseBody): InputStream {

                return responseBody.byteStream()
            }

}
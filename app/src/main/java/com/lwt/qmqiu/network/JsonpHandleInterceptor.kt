package com.lwt.qmqiu.network


import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class JsonpHandleInterceptor : Interceptor  {

    override fun intercept(chain: Interceptor.Chain?) : Response  {
        try {

            var response = chain!!.proceed(chain.request())
            var responseBody = response.body()
            var mediaType = responseBody?.contentType()
            var content = responseBody?.string()
            var index = content?.indexOf("(") as Int
            if ( index != -1) {
                content = content?.substring(index + 1, content.length - 1)
            }
           // Logger.e(content)
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build()
        }catch (e:Exception){

            throw e
        }

    }
}
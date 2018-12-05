package com.lwt.qmqiu.network


import com.lwt.qmqiu.bean.HttpResult
import com.lwt.qmqiu.utils.UiUtils
import io.reactivex.functions.Function


class HttpResultFunc<T> : Function<HttpResult<T>, T> {

            @Throws(Exception::class)
            override fun apply(tHttpResult: HttpResult<T>): T {

                if (tHttpResult.code != 200) {
                    throw ApiException(tHttpResult.code, tHttpResult.message)
                }

                return tHttpResult.data
            }

}
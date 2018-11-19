package com.lwt.qmqiu.download

import android.util.Log
import com.orhanobut.logger.Logger
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*


class DownloadResponseBody(responseBody:ResponseBody,downloadListener:DownloadListen): ResponseBody() {

    private var responseBody:ResponseBody = responseBody

    private var downloadListener:DownloadListen = downloadListener

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private var bufferedSource: BufferedSource?=null

    override fun  contentType(): MediaType {
        return responseBody.contentType()!!
    }


    override fun contentLength():Long {
        return responseBody.contentLength()
    }

    override fun source():BufferedSource {

        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody!!.source()!!))
        }
        return bufferedSource!!
    }

    private  fun source(source:Source): Source {

        return object :ForwardingSource(source){

            var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {

                var bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0;

                var progress = (totalBytesRead * 100 / responseBody.contentLength()).toInt()

                if (null != downloadListener) {

                    if (bytesRead != -1L) {
                        downloadListener.onProgress(progress)
                    }

                }
                return bytesRead

            }
        }

    }

}
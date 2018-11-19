package com.lwt.qmqiu.download

interface DownloadListen {

    fun onStartDownload()

    fun onProgress(progress: Int)

    fun onFinishDownload(path:String)

    fun onFail(errorInfo: String)
}
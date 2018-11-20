package com.lwt.qmqiu.download


import android.os.Environment
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.network.HttpResultFunc
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.voice.VoiceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.*
import java.util.concurrent.TimeUnit

class DownloadManager {

    companion object {

        private val DEFAULT_TIMEOUT = 15L

        private lateinit var retrofit: Retrofit

        private var FILE_PATH = Environment.getExternalStorageDirectory().absolutePath+"/qmqiu/download/"
    }


    private  var listener:DownloadListen
    private  var mInterceptor:DownloadInterceptor

    constructor(listener:DownloadListen){

        this.listener = listener

         mInterceptor = DownloadInterceptor(listener)

        var httpClient = OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL_Api)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        var file = File(FILE_PATH)
        if (!file.exists()){
            file.mkdirs()
        }
    }

    /**
     * 开始下载
     *
     * @param url
     * @param filePath
     * @param subscriber
     */
    fun download(id:String) {

        listener.onStartDownload()

        var filePath:String=""

        retrofit.create(ApiService::class.java).download(id)?.map(DownloadResultFunc()).doOnNext {

            filePath= writeFile(it, mInterceptor.fileName)

        }.applySchedulers().subscribe({

            listener.onFinishDownload(filePath)

        },{


        })


    }

    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private fun writeFile(inputString:InputStream,fileName:String):String {

        var file =  File(FILE_PATH,fileName)
        if (file.exists()) {
            file.delete()
        }

        var fos :FileOutputStream
        try {
            fos = FileOutputStream(file)

            var b =  ByteArray(1024)

            var len:Int

            do {

                len = inputString.read(b)

                if (len!=-1)
                    fos.write(b,0,len)

            } while(len!=-1)

            inputString.close()

            fos.close()

        } catch ( e:Exception) {
            listener.onFail(e.localizedMessage)
        }
        return file.absolutePath
    }

}
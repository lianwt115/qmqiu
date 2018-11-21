package com.lwt.qmqiu.download


import android.os.Environment
import com.lwt.qmqiu.bean.DownloadFileDb
import com.lwt.qmqiu.dao.DownloadFileDbUtils
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.applySchedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    private  lateinit var mInterceptor:DownloadInterceptor

    constructor(listener:DownloadListen,id: String,type:Int){

        this.listener = listener


        var path = checkFileExits(id,type)

        if (path!=null) {

            listener.onFinishDownload(path)

        }else{

            init()

            download(id,type)

        }

    }

    private fun  checkFileExits(id: String, type: Int):String?{

        var obj = DownloadFileDbUtils.findByIdOne(id)

        //有记录
        if (obj != null && File(obj.filePath).exists() && type == obj.fileType)

            return obj.filePath

        return null
    }

    private fun  init(){

        mInterceptor = DownloadInterceptor(listener)

        var httpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
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
    private fun download(id: String, type: Int) {

        listener.onStartDownload()

        var filePath:String=""

        retrofit.create(ApiService::class.java).download(id)?.map(DownloadResultFunc()).doOnNext {

            filePath= writeFile(it, mInterceptor.fileName)

        }.applySchedulers().subscribe({

            //下载完成后,将记录写入数据库

            DownloadFileDbUtils.insertOrReplace(DownloadFileDb(null,mInterceptor.fileName,id,System.currentTimeMillis(),type,filePath))

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
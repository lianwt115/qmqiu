package com.lwt.qmqiu.activity


import android.os.Bundle
import androidx.core.view.ViewCompat
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.lwt.qmqiu.shareelement.ShareContentInfo
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_videoplay.*
import java.util.concurrent.TimeUnit


class VideoPlayActivity:BaseActivity(), IShareElements {



    private lateinit var mData:PhotoViewData

    override fun onCreate(savedInstanceState: Bundle?) {

        YcShareElement.setEnterTransitions(this, this, true)

        super.onCreate(savedInstanceState)

        //changeStatusColor(R.color.black)

        setContentView(R.layout.activity_videoplay)

        mData =intent.getParcelableExtra<PhotoViewData>("photoViewData")

        val data = App.instanceApp().getShowMessage(mData.content)

        ViewCompat.setTransitionName(preview_video,data)

        var dataAll = data.split("_ALWTA_")

        DownloadManager(object : DownloadListen {
            override fun onStartDownload() {
                Logger.e("onStartDownload")
            }

            override fun onProgress(progress: Int) {

            }

            override fun onFinishDownload(path: String) {

                //进行播放
                preview_video.register(this@VideoPlayActivity)
                preview_video.setVideoPath(path,true)

            }

            override fun onFail(errorInfo: String) {
                Logger.e("onFail:$errorInfo")
            }
        },dataAll[0],5)


        YcShareElement.postStartTransition(this)

        back_iv.setOnClickListener {

            super.onBackPressed()
        }
    }

    //zhege
    override fun getShareElements(): Array<ShareElementInfo<PhotoViewData>> {


        return arrayOf(ShareContentInfo(preview_video,mData))
    }

    override fun finishAfterTransition() {
        YcShareElement.finishAfterTransition(this, this)
        super.finishAfterTransition()
    }


    override fun onResume() {
        super.onResume()
        preview_video.onResume()
    }

    override fun onStop() {
        super.onStop()
        preview_video.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
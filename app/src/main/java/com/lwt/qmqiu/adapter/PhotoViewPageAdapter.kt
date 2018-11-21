package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.orhanobut.logger.Logger

class PhotoViewPageAdapter(context:Context,list: List<PhotoViewData>,var listen:PhotoSingleClick?=null): PagerAdapter() {

    private var mList  = list
    private var mContext  = context
    private var mListen  = listen


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val photoView = PhotoView(container.context)

        var data = App.instanceApp().getShowMessage(mList[position].content).split("_ALWTA_")

        var down = DownloadManager(object : DownloadListen {
            override fun onStartDownload() {
                Logger.e("onStartDownload")
            }

            override fun onProgress(progress: Int) {

            }

            override fun onFinishDownload(path: String) {

                Glide.with(mContext).load(path).into(photoView)

            }

            override fun onFail(errorInfo: String) {
                Logger.e("onFail:$errorInfo")
            }
        },data[0],4)

        photoView.setOnClickListener {

            if (mListen!=null)
                mListen!!.photoViewSingleClick()
        }

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        return photoView

    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {

        return view === obj
    }

    override fun getCount(): Int {
       return mList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {

        container.removeView(obj as View)

    }

    interface PhotoSingleClick{
        fun photoViewSingleClick()
    }
}
package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.lwt.qmqiu.shareelement.ShareContentInfo
import com.orhanobut.logger.Logger

class PhotoViewPageAdapter(context: Context, list: List<PhotoViewData>, var listen: PhotoSingleClick? = null, real: Boolean): PagerAdapter() {

    private var mList  = list
    private var mViewList  = ArrayList<ViewData>()
    private var mContext  = context
    private var mListen  = listen
    //real是否是加密数据
    private var mReal  = real


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val photoView = ImageView(container.context)

        mViewList.add(ViewData(photoView,position))

        val data =if (mReal) App.instanceApp().getShowMessage(mList[position].content) else mList[position].content

        ViewCompat.setTransitionName(photoView,data)

        var dataAll = data.split("_ALWTA_")

        DownloadManager(object : DownloadListen {
            override fun onStartDownload() {
                Logger.e("onStartDownload")
            }

            override fun onProgress(progress: Int) {

            }

            override fun onFinishDownload(path: String, type: Int) {

                Glide.with(mContext).load(path).into(photoView)

            }

            override fun onFail(errorInfo: String) {
                Logger.e("onFail:$errorInfo")
            }
        },dataAll[0],4)

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

    fun getCurrentObj(index:Int): ShareContentInfo {

        return ShareContentInfo(getViewObj(index),mList[index])

    }

    fun getViewObj(index:Int): View {

        mViewList.forEach {

            if (index == it.realIndex)
                return it.view
        }

        return mViewList[0].view
    }



    interface PhotoSingleClick{
        fun photoViewSingleClick()
    }

    data class ViewData(var view:View,var realIndex:Int)
}
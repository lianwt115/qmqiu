package com.lwt.qmqiu.activity

import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.IMListAdapter
import com.lwt.qmqiu.adapter.VideoListAdapter
import com.lwt.qmqiu.bean.QMMessage
import com.orhanobut.logger.Logger
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import com.lwt.qmqiu.bean.VideoSurface
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.BarView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_im.*
import java.util.concurrent.TimeUnit



class IMActivity : BaseActivity(),MapLocationUtils.FindMeListen, View.OnClickListener, IMListAdapter.IMClickListen, QMWebsocket.QMMessageListen, BarView.BarOnClickListener {



    private var leaveTime = 0

    override fun locationInfo(location: BDLocation?) {

        val now1 = location!!.longitude*1000
        val now2 = location.latitude*1000
        val now = now1.toString().split(".")[0].plus(now2.toString().split(".")[0])

        val local1 = mBDLocation!!.longitude*1000
        val local2 = mBDLocation.latitude*1000
        val local = local1.toString().split(".")[0].plus(local2.toString().split(".")[0])

        if(now != local){
            if (leaveTime <3)
                leaveTime++

            UiUtils.showToast("第$leaveTime,三次后将自动退出")

            if (leaveTime == 3){
                Observable.timer(2,TimeUnit.SECONDS).applySchedulers().subscribe({
                    finish()
                },{
                    Logger.e("退出异常")
                })
            }

        }

       // Logger.e("离开区域:${now != local}")

    }


    private lateinit var mRtcEngine:RtcEngine
    private lateinit var mBDLocation:BDLocation
    private lateinit var mVideoListAdapter:VideoListAdapter
    private lateinit var mIMListAdapter:IMListAdapter
    private lateinit var mDisposable: Disposable
    private  var mVideoSurfaceList = ArrayList<VideoSurface>()
    private  var mIMMessageList = ArrayList<QMMessage>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_im)

        mBDLocation=intent.getParcelableExtra<BDLocation>("location")

        Logger.e("纬度:${mBDLocation.latitude}**经度:${mBDLocation.longitude}")

        initRecycleView()

        mDisposable = Observable.interval(2,TimeUnit.SECONDS).applySchedulers().subscribe({

            MapLocationUtils.getInstance().findMe(this)

        },{

            Logger.e("持续定位失败")
        })

        im_bt.setOnClickListener(this)

        //地址(当前人数,连接时websocket返回)
        im_barview.changeTitle(mBDLocation.city.plus(mBDLocation.district).plus(mBDLocation.street))
        im_barview.setBarOnClickListener(this)
        QMWebsocket.getInstance().setMessageListen(this)
    }

    private fun initRecycleView() {


        val linearLayoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_im.layoutManager=linearLayoutManager

        mIMListAdapter= IMListAdapter(this,mIMMessageList,this)

        recycleview_im.adapter = mIMListAdapter

        recycleview_im.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })
    }




    override fun onDestroy() {
        super.onDestroy()
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }

        MapLocationUtils.getInstance().exit()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.im_bt -> {


                        if (im_et.text.isEmpty())
                            return

                        val message = QMMessage()

                        message.message = im_et.text.toString()

                       /* mIMMessageList.add(message)

                        mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)

                        recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)*/

                        im_et.setText("")

                        QMWebsocket.getInstance().sengText(message)


            }


        }
    }


    override fun imClick(content: VideoSurface, position: Int) {

    }

    override fun qmMessage(message: QMMessage) {

        runOnUiThread {

            mIMMessageList.add(message)

            mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)

            recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)

            Logger.e(message.toString())
        }

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {
                finish()
            }

            false -> {
                UiUtils.showToast("点击更多")
            }
        }
    }


}
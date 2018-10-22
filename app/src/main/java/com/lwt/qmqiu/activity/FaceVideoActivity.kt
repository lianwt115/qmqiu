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
import com.lwt.qmqiu.R.id.*
import com.lwt.qmqiu.adapter.IMListAdapter
import com.lwt.qmqiu.adapter.VideoListAdapter
import com.lwt.qmqiu.bean.QMMessage
import com.orhanobut.logger.Logger
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoEncoderConfiguration
import io.agora.rtc.video.VideoCanvas
import com.lwt.qmqiu.bean.VideoSurface
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_facevideo.*
import java.util.concurrent.TimeUnit



class FaceVideoActivity : BaseActivity(), VideoListAdapter.TextClickListen, MapLocationUtils.FindMeListen, View.OnClickListener, IMListAdapter.IMClickListen, QMWebsocket.QMMessageListen {



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

        setContentView(R.layout.activity_facevideo)

        mBDLocation=intent.getParcelableExtra<BDLocation>("location")

        Logger.e("纬度:${mBDLocation.latitude}**经度:${mBDLocation.longitude}")

        initRecycleView()

        initializeAgoraEngine()
        setupLocalVideo()
        joinChannel()


        mDisposable = Observable.interval(2,TimeUnit.SECONDS).applySchedulers().subscribe({

            MapLocationUtils.getInstance().findMe(this)

        },{

            Logger.e("持续定位失败")
        })

        im_bt.setOnClickListener(this)

        QMWebsocket.getInstance().setMessageListen(this)
    }

    private fun initRecycleView() {
        val gridLayoutManager = object :GridLayoutManager(this, 3){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_video.layoutManager=gridLayoutManager
        mVideoListAdapter= VideoListAdapter(this,mVideoSurfaceList,this)

        recycleview_video.adapter = mVideoListAdapter

        recycleview_video.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })

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


    private var mRtcEventHandler =object : IRtcEngineEventHandler(){



        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            Logger.e("onFirstRemoteVideoDecoded:uid-$uid")
            runOnUiThread {

                setupRemoteVideo(uid)
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            Logger.e("onUserOffline:uid-$uid")
            runOnUiThread {
                onRemoteUserLeft(uid)
            }
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)

            Logger.e("onUserJoined:uid-$uid")
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)

            Logger.e("onJoinChannelSuccess:uid-$uid ** channel:$channel")
        }

    }

    private fun onRemoteUserLeft(uid: Int) {

       /* val container = video_remote

        container.removeAllViews()*/

        val obj = checkContain(uid)

        if (obj != null) {

            mVideoSurfaceList.remove(obj)

            mVideoListAdapter.notifyDataSetChanged()
        }

    }


    private fun initializeAgoraEngine() {

        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)

            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)


            val orientationMode = VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT

            val dimensions = VideoEncoderConfiguration.VideoDimensions(360, 640)

            val videoEncoderConfiguration = VideoEncoderConfiguration(dimensions, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30, 0, orientationMode)

            mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration)

        } catch (e: Exception) {

            Logger.e(Log.getStackTraceString(e))

            throw RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e))
        }

    }

    private fun setupLocalVideo() {

        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        //video_local.addView(surfaceView)
        mRtcEngine.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))

        mVideoSurfaceList.add(VideoSurface(0,surfaceView))

        mVideoListAdapter.notifyDataSetChanged()
    }

    private fun joinChannel(uid: Int =0) {

        val channel1 = "lwt"
        val channel2 = mBDLocation.longitude*1000
        val channel3 = mBDLocation.latitude*1000


        val channel = channel1.plus(channel2.toString().split(".")[0]).plus(channel3.toString().split(".")[0])
        mRtcEngine.joinChannel(null,channel,"lwt", 0) // if you do not specify the uid, Agora will assign one.

        Logger.e(channel)
        Logger.e(channel2.toString())
        Logger.e(channel3.toString())

    }

    private fun setupRemoteVideo(uid: Int) {

       /* val container = video_remote

        if (container.childCount >= 1) {
            return
        }*/

        if(checkContain(uid) == null && mVideoSurfaceList.size<=6){

            val surfaceView = RtcEngine.CreateRendererView(baseContext)
            //container.addView(surfaceView)
            mRtcEngine.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid))


            mVideoSurfaceList.add(VideoSurface(uid,surfaceView))

            mVideoListAdapter.notifyDataSetChanged()
        }

    }

    private fun leaveChannel() {
        mRtcEngine.leaveChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }

        MapLocationUtils.getInstance().exit()
        leaveChannel()
        RtcEngine.destroy()
    }

    override fun onResume() {
        super.onResume()
        mRtcEngine.enableAudio()
        mRtcEngine.enableVideo()
    }

    override fun onStop() {
        super.onStop()
        mRtcEngine.disableAudio()
        mRtcEngine.disableVideo()

    }


    override fun textClick(content: VideoSurface, position: Int) {

        Logger.e(content.uid.toString())
    }

    fun checkContain(uid:Int):VideoSurface?{

        mVideoSurfaceList.forEach {

            if(it.uid == uid)

                return  it

        }

        return null
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


}
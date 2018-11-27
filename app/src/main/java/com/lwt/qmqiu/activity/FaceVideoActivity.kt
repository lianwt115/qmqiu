package com.lwt.qmqiu.activity


import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import cn.dreamtobe.kpswitch.IFSPanelConflictLayout
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.VideoListAdapter
import com.lwt.qmqiu.bean.QMMessage
import com.orhanobut.logger.Logger
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoEncoderConfiguration
import io.agora.rtc.video.VideoCanvas
import com.lwt.qmqiu.bean.VideoSurface
import com.lwt.qmqiu.mvp.contract.FaceVideoContract
import com.lwt.qmqiu.mvp.present.FaceVideoPresent
import com.lwt.qmqiu.mvp.present.RoomMessagePresent
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_facevideo.*
import java.util.concurrent.TimeUnit


class FaceVideoActivity : BaseActivity(), View.OnClickListener, FaceVideoContract.View {


    override fun setEexitVideoRequest(success: Boolean) {

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

    }

    private lateinit var mRtcEngine:RtcEngine

    private  var mChannel:String?=null
    private  var obj:QMMessage?=null
    private  var mDisposable: Disposable?=null
    private  var mDisposableTime: Disposable?=null
    private lateinit var present: FaceVideoPresent
    private  var mActive:Boolean =  false
    private  var mCallTime =  0
    private lateinit var mVibrator:Vibrator
    private val patter = longArrayOf(1000, 1000, 1000, 1000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeStatusColor(R.color.callvideo_bg)

        setContentView(R.layout.activity_facevideo)

        mVibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        mChannel = intent.getStringExtra("videoChannel")
        mActive = intent.getBooleanExtra("active",false)

        obj = intent.getParcelableExtra<QMMessage>("message")

        if (obj!=null){

            //显示头像信息
            root_who.visibility = View.VISIBLE

            Glide.with(this).load(ApiService.BASE_URL_Api.plus(obj!!.imgPath)).into(img_who)

            name_who.text = obj!!.from

        }

        if (mChannel!=null){

            initializeAgoraEngine()

            setupLocalVideo()

            if (!mActive)

                joinChannel()

            else{

                mVibrator.vibrate(patter, 0)

                acceptvideo.visibility = View.VISIBLE

            }


        }else{

            showProgressDialog("视频通话异常")

            exit()

        }

        refusevideo.setOnClickListener(this)
        acceptvideo.setOnClickListener(this)

        present = FaceVideoPresent(this,this)
    }

    private fun  exit(){

        mDisposable =  Observable.timer(2,TimeUnit.SECONDS).applySchedulers().subscribe({

                finish()


        },{

            Logger.e(it.localizedMessage)
        })

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.acceptvideo -> {

                mVibrator.cancel()

                joinChannel()

                acceptvideo.visibility =View.GONE
            }

            R.id.refusevideo -> {

                present.exitVideoRequest(obj!!.message, App.instanceApp().getLocalUser()?.name?:"xxx",mCallTime)

                mVibrator.cancel()

                finish()
            }
        }

    }


    private var mRtcEventHandler =object : IRtcEngineEventHandler(){

        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            Logger.e("onFirstRemoteVideoDecoded:uid-$uid")
            runOnUiThread {

                countTime()
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

    private fun countTime() {

        mDisposableTime = Observable.interval(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            mCallTime++


        },{

            Logger.e(it.localizedMessage)

        })




    }

    private fun onRemoteUserLeft(uid: Int) {

        video_contain_big.removeAllViews()

        showProgressDialog("用户已离开")

        exit()

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
        video_contain_small.addView(surfaceView)
        mRtcEngine.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))


    }

    private fun joinChannel() {

        mRtcEngine.joinChannel(null,mChannel,"lwt", 0) // if you do not specify the uid, Agora will assign one.

    }

    private fun setupRemoteVideo(uid: Int) {

        val container = video_contain_big

        if (container.childCount >= 1) {
            return
        }


        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        mRtcEngine.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid))


    }

    private fun leaveChannel() {
        mRtcEngine.leaveChannel()
    }

    override fun onDestroy() {
        super.onDestroy()

        mVibrator.cancel()

        leaveChannel()

        RtcEngine.destroy()

        if (mDisposable!=null && !mDisposable!!.isDisposed)
            mDisposable!!.dispose()

        if (mDisposableTime!=null && !mDisposableTime!!.isDisposed)
            mDisposableTime!!.dispose()
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

}
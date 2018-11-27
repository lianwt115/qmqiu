package com.lwt.qmqiu.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.lwt.qmqiu.R
import com.lwt.qmqiu.voice.VoiceManager
import kotlinx.android.synthetic.main.widget_voiceview.view.*
import androidx.core.os.HandlerCompat.postDelayed
import java.io.File


/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class VoiceView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr) {


    private  var voiceManager = VoiceManager.getInstance()

    private var maxRecordTime = (1000 * 60 * 60 * 24).toLong()
    private var recordTime: Long = 0
    private val UPDATE_TIME = 200

    private val mHandler = Handler()
    private var mCancle = false

    private var mStop = false
    private var mStart = false

    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_voiceview, null)

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    private fun updateRecordingUI() {

        var drawable = context.getDrawable(R.mipmap.voice_1)

        var bg = context.getDrawable(R.drawable.bg_0dp_voiceview_normal)

        var text = "手指上滑,取消发送"

        if (mCancle){

            drawable = context.getDrawable(R.mipmap.voice_cancle)

            text = "松开手指,取消发送"

            bg = context.getDrawable(R.drawable.bg_0dp_voiceview_cancle)

        }else{

            //更新ui
            when (voiceManager.getVolume()) {

                2 -> {
                    drawable = context.getDrawable(R.mipmap.voice_2)
                }
                3 -> {
                    drawable = context.getDrawable(R.mipmap.voice_3)
                }
                4 -> {
                    drawable = context.getDrawable(R.mipmap.voice_4)
                }
                5 -> {
                    drawable = context.getDrawable(R.mipmap.voice_5)
                }
                6 -> {
                    drawable = context.getDrawable(R.mipmap.voice_6)
                }
                7 -> {
                    drawable = context.getDrawable(R.mipmap.voice_7)
                }
            }
        }


        voice_img.setImageDrawable(drawable)
        voice_text.text = text
        voice_text.background = bg


    }

    /**
     * 录音更新进度条
     */
    private val mRecordProgressTask = object : Runnable {
        override fun run() {

            //录音时间超出最大时间，自动停止
            if (recordTime > maxRecordTime) {
                voiceManager.stopRecord(true)
            } else {
                updateRecordingUI()
                mHandler?.postDelayed(this, UPDATE_TIME.toLong())
            }
        }
    }

    //停止录音按钮状态
    fun stopRecord(isFromUser: Boolean) {
        mHandler?.removeCallbacksAndMessages(null)
        try {
            recordTime = 0
            updateStopRecordUI()

            voiceManager.stopRecord(isFromUser)

            mStart = false

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun updateStopRecordUI() {
        //停止视图消失

        var drawable = context.getDrawable(R.mipmap.voice_1)

        var bg = context.getDrawable(R.drawable.bg_0dp_voiceview_normal)

        var text = "手指上滑,取消发送"

        voice_img.setImageDrawable(drawable)
        voice_text.text = text
        voice_text.background = bg

        mCancle = false

        voiceview_root.visibility = View.GONE

        mStop = true
    }

    fun stopRecord() {
        //异常情况下停止
        if (!mStop && mStart)
            stopRecord(false)
    }

    //开始录音
    fun startRecord(fileName: String,listen: VoiceManager.VoiceRecordListen?=null) {
        mStart = true
        updateStartRecordUI()
        try {
            voiceManager.startRecord(fileName,listen)
            mHandler?.post(mRecordProgressTask)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun updateStartRecordUI() {
        //视图显示
        mStop = false
        voiceview_root.visibility = View.VISIBLE

    }

    fun  setCancle(cancle:Boolean){

        this.mCancle = cancle

    }


    //设置录音的最大时间
    fun setMaxRecordTime(millis: Long) {
        maxRecordTime = millis
    }

}
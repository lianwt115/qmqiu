package com.lwt.qmqiu.voice

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.orhanobut.logger.Logger
import java.io.IOException
import android.media.MediaPlayer
import android.os.Build.VERSION_CODES.BASE
import cn.dreamtobe.kpswitch.IFSPanelConflictLayout
import com.lwt.qmqiu.utils.RxBus
import java.io.File


class VoiceManager {


    companion object {

        @Volatile
        private  var mInstance: VoiceManager?=null

        private  var mMediaRecorder: MediaRecorder?=null

        //最大时长
        private var MAX_LENGTH = 20*1000

        private var FILE_PATH = Environment.getExternalStorageDirectory().absolutePath+"/qmqiu/voice/"

        fun getInstance(): VoiceManager {
            if (mInstance == null) {
                synchronized(VoiceManager::class.java) {
                    if (mInstance == null) {
                        mInstance = VoiceManager()
                        mMediaRecorder = MediaRecorder()
                        var file = File(FILE_PATH)
                        if (!file.exists()){
                            file.mkdirs()
                        }
                    }
                }
            }
            return mInstance!!
        }

    }

    private lateinit  var  mFile: File

    private   var  mVoiceRecordListen: VoiceRecordListen?= null
    private   var  mStartTime: Long = 0L
    private   var  mEndTime: Long = 0L
    var MAX_VOICE = 50
    var BASE = 600
        /**
         * 开始录音 使用amr格式
         * 录音文件
         *
         * @return
         */
        fun startRecord(fileName: String,listen:VoiceRecordListen?=null) {

            this.mVoiceRecordListen = listen

            mFile = File(FILE_PATH,fileName.plus(".aac"))

            // 开始录音
            /* ①Initial：实例化MediaRecorder对象 */
            try {

                if (mMediaRecorder == null)

                    mMediaRecorder = MediaRecorder()


                /* ②setAudioSource/setVedioSource */
                mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)// 设置麦克风
                /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
                mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
                mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                /* ③准备 */
                mMediaRecorder?.setOutputFile(mFile)
                mMediaRecorder?.setMaxDuration(MAX_LENGTH)
                mMediaRecorder?.prepare()
                /* ④开始 */
                mMediaRecorder?.start()
                mStartTime = System.currentTimeMillis()
                if (mVoiceRecordListen!=null)
                    mVoiceRecordListen!!.start()

                // AudioRecord audioRecord.
                Logger.e( "startTime${mStartTime}")
            } catch (e:Exception) {

                if (mVoiceRecordListen!=null)
                    mVoiceRecordListen!!.err("startAmr:${e.message}")

            }

        }


        fun stopRecord(ok: Boolean) {

            // 开始录音
            /* ①Initial：实例化MediaRecorder对象 */
            try {

                mMediaRecorder?.stop()

                mMediaRecorder?.reset()


                mEndTime = System.currentTimeMillis()
                    // AudioRecord audioRecord.
                Logger.e( "mEndTime${mEndTime}")
                if (mVoiceRecordListen!=null && ok)
                    mVoiceRecordListen!!.finished(mFile,(mEndTime-mStartTime).toInt()/1000+1)

                else
                    mFile.delete()

            } catch (e: Exception) {

                mMediaRecorder = null

                if (mVoiceRecordListen!=null)
                    mVoiceRecordListen!!.err("录制时间过短")

            }

        }

      fun getVolume():Int{

          val ratio = mMediaRecorder?.maxAmplitude!! / BASE
          var db = 0// 分贝 也可以理解为定义的音量大小

          if (ratio > 1){

              db = (30 * Math.log10(ratio.toDouble())).toInt()

          }

          return (db/7 +1)
      }



    interface VoiceRecordListen{

        fun start()

        fun finished(file: File,time:Int)

        fun err(errMessage: String)



    }

}
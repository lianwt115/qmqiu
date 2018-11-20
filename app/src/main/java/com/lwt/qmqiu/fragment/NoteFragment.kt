package com.lwt.qmqiu.fragment

import com.lwt.qmqiu.R
import com.lwt.qmqiu.voice.VoiceManager
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_note.*
import java.io.File


class NoteFragment: BaseFragment(){


    override fun getLayoutResources(): Int {

        return  R.layout.fragment_note
    }

    override fun initView() {


        startBtn.setOnClickListener{


            voice_view.startRecord("test",object :VoiceManager.VoiceRecordListen{

                override fun finished(file: File, time: Int) {
                    Logger.e(file.absolutePath)
                }

                override fun err(errMessage: String) {
                    Logger.e(errMessage)
                }

                override fun start() {
                    Logger.e("开始")
                }

            })


        }

        stopBtn.setOnClickListener {

            voice_view.stopRecord(true)

        }
        outside.setOnClickListener {

            voice_view.setCancle(true)

        }
        inside.setOnClickListener {

            voice_view.setCancle(false)

        }

    }

    override fun onStop() {
        super.onStop()
        voice_view.stopRecord()
    }


}
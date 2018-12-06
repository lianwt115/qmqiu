package com.lwt.qmqiu.activity




import android.os.Bundle
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseActivity(),BarView.BarOnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        setting_barview.setBarOnClickListener(this)
        setting_barview.changeTitle("设置")

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

}
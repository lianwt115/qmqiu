package com.lwt.qmqiu.activity




import android.os.Bundle
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.NoticeDialog
import com.lwt.qmqiu.widget.ShowListView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseActivity(),BarView.BarOnClickListener, ShowListView.ShowListOnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        setting_barview.setBarOnClickListener(this)
        setting_barview.changeTitle("设置")

        use_notice.changeTitleAndContent("使用须知")
        use_notice.setBarOnClickListener(this,use_notice.id)
        exit.changeTitleAndContent("退出")
        exit.setBarOnClickListener(this,exit.id)

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

    override fun showListViewClick(long: Boolean, content: String, id: Int) {

        when (id) {

            R.id.use_notice -> {

                Logger.e("使用须知")

            }

            R.id.exit -> {

               showProgressDialog("确认退出",true,2,object :NoticeDialog.Builder.BtClickListen{

                   override fun btClick(etContent: String): Boolean {

                       App.instanceApp().userExit()

                       finish()

                       return false

                   }

               })

            }
        }
    }

}
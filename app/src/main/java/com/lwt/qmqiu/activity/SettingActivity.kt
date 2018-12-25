package com.lwt.qmqiu.activity



import android.os.Bundle
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.NoticeDialog
import com.lwt.qmqiu.widget.ShowListView
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseActivity(),BarView.BarOnClickListener, ShowListView.ShowListOnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        setting_barview.setBarOnClickListener(this)
        setting_barview.changeTitle(getString(R.string.setting))


        exit.changeTitleAndContent(getString(R.string.setting_exit))
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


            R.id.exit -> {

               showProgressDialog(getString(R.string.setting_exit_sure),true,2,object :NoticeDialog.Builder.BtClickListen{

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
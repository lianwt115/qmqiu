package com.lwt.qmqiu.activity




import android.os.Bundle
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_giftinfo.*




class GiftInfoActivity : BaseActivity(),BarView.BarOnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_giftinfo)

        giftinfo_barview.setBarOnClickListener(this)
        giftinfo_barview.changeTitle("礼物详情")

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

}
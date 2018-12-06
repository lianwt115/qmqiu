package com.lwt.qmqiu.activity




import android.os.Bundle
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_charge.*


class ChargeActivity : BaseActivity(),BarView.BarOnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_charge)

        charge_barview.setBarOnClickListener(this)
        charge_barview.changeTitle("充值详情")

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

}
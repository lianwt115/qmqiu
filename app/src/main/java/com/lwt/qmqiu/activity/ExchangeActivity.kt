package com.lwt.qmqiu.activity




import android.os.Bundle
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_exchange.*




class ExchangeActivity : BaseActivity(),BarView.BarOnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_exchange)

        exchange_barview.setBarOnClickListener(this)
        exchange_barview.changeTitle("兑换详情")

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

}
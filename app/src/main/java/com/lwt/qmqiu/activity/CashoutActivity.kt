package com.lwt.qmqiu.activity



import android.os.Bundle
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_cashout.*



class CashoutActivity : BaseActivity(),BarView.BarOnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cashout)

        cashout_barview.setBarOnClickListener(this)
        cashout_barview.changeTitle(getString(R.string.cash))

        setUser()


    }
    private fun setUser() {

        var baseUser = App.instanceApp().getLocalUser()

        if (baseUser!=null){

            //图像
            Glide.with(this).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)

            //修改为showname
            user_name.changeTitleAndContent(baseUser.showName,"")

            user_basecoin.changeTitleAndContent(getString(R.string.coinbase),baseUser.coinbase.toString())
            user_coin.changeTitleAndContent(getString(R.string.coin),baseUser.coin.toString())
            user_coinexchange.changeTitleAndContent(getString(R.string.cash_coin_exchange),baseUser.coinExchange.toString())

        }

    }

    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }


}
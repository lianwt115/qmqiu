package com.lwt.qmqiu.activity





import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.StaticValues
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_cashout.*



class CashoutActivity : BaseActivity(),BarView.BarOnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cashout)

        cashout_barview.setBarOnClickListener(this)
        cashout_barview.changeTitle("提现")

        setUser()


    }
    private fun setUser() {

        var baseUser = App.instanceApp().getLocalUser()

        if (baseUser!=null){

            //图像
            Glide.with(this).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)

            //修改为showname
            user_name.changeTitleAndContent(baseUser.showName,"")

            user_basecoin.changeTitleAndContent("青木",baseUser.coinbase.toString())
            user_coin.changeTitleAndContent("青木球",baseUser.coin.toString())
            user_coinexchange.changeTitleAndContent("可提现青木球",baseUser.coinExchange.toString())

        }

    }



    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()


    }

}
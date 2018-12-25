package com.lwt.qmqiu.activity




import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.ClipboardManager
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.ChargeContract
import com.lwt.qmqiu.mvp.present.ChargePresent
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.StaticValues
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.BarView
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_charge.*
import java.util.concurrent.TimeUnit


class ChargeActivity : BaseActivity(),BarView.BarOnClickListener, View.OnClickListener, ChargeContract.View {

    override fun setChargeNum(chargeNum: String) {

        creatcharge_num.text = chargeNum

        coin_num.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(500, TimeUnit.MILLISECONDS).applySchedulers().subscribe({

            coin_num.revertAnimation()

        },{
            Logger.e(it.localizedMessage)
        })

    }

    override fun setCoinCharge(baseUser: BaseUser) {

        App.instanceApp().updataLocalUser(baseUser,false,true)

        coin_charge.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(500, TimeUnit.MILLISECONDS).applySchedulers().subscribe({

            coin_charge.revertAnimation()

            setUser()

        },{
            Logger.e(it.localizedMessage)
        })

        UiUtils.showToast(getString(R.string.charge_success))
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {
            //充值码
            2 -> {

                coin_num!!.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1000,TimeUnit.MILLISECONDS).applySchedulers().subscribe({

                    coin_num.revertAnimation()

                },{
                    Logger.e(it.localizedMessage)
                })

            }
            //充值
            3 -> {

                coin_charge!!.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1000,TimeUnit.MILLISECONDS).applySchedulers().subscribe({

                    coin_charge.revertAnimation()

                },{
                    Logger.e(it.localizedMessage)
                })

            }
        }

        UiUtils.showToast(errMessage?:getString(R.string.sys_err))
    }


    private lateinit var present: ChargePresent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_charge)

        present = ChargePresent(this,this)

        charge_barview.setBarOnClickListener(this)
        charge_barview.changeTitle(getString(R.string.charge))
        charge_barview.showMore(true)
        setUser()

        coin_charge.text = getString(R.string.charge)
        coin_charge.background = getDrawable(R.drawable.bg_20dp_13)
        coin_charge.setFinalCornerRadius(20F)
        coin_charge.setOnClickListener(this)

        coin_num.text = getString(R.string.create_charge_number)
        coin_num.background = getDrawable(R.drawable.bg_20dp_13)
        coin_num.setFinalCornerRadius(20F)
        coin_num.setOnClickListener(this)

        charge_num.setOnLongClickListener {

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 将文本内容放到系统剪贴板里。

            if (TextUtils.isEmpty(cm.text))

                UiUtils.showToast(getString(R.string.copy_content_is_empty))

            else{
                charge_num.text = cm.text

            }


            true
        }

        creatcharge_num.setOnLongClickListener {

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 将文本内容放到系统剪贴板里。

            if (!TextUtils.isEmpty(creatcharge_num.text)){

                cm.text = creatcharge_num.text

                UiUtils.showToast(getString(R.string.have_copy))
            }


          true

        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.coin_charge -> {

                var chargeNum = charge_num.text.toString()

                if (!TextUtils.isEmpty(chargeNum)){

                    var baseUser = App.instanceApp().getLocalUser()

                    if (baseUser!=null){

                        coin_charge.startAnimation()

                        present.coinCharge(baseUser.name,chargeNum,bindToLifecycle())

                    }else{

                        UiUtils.showToast(getString(R.string.please_login))
                    }

                }else{

                    UiUtils.showToast(getString(R.string.charge_number_empty))
                }


            }

            R.id.coin_num -> {

                    var chargeCount = charge_count.text.toString()

                    if (!TextUtils.isEmpty(chargeCount)){

                        var baseUser = App.instanceApp().getLocalUser()

                        if (baseUser!=null){

                            coin_num.startAnimation()

                            present.creatChargeNum(baseUser.name,1,baseUser.publicKey,chargeCount.toInt(),bindToLifecycle())

                        }else{

                            UiUtils.showToast(getString(R.string.please_login))
                        }


                    }else{

                        UiUtils.showToast(getString(R.string.please_input_cash))

                    }

            }


        }
    }




    private fun setUser() {

        var baseUser = App.instanceApp().getLocalUser()

        if (baseUser!=null){

            creatchargenum_root.visibility = if (baseUser.name == StaticValues.AdminName) View.VISIBLE else View.GONE

            //图像
            Glide.with(this).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)

            //修改为showname
            user_name.changeTitleAndContent(baseUser.showName,"")

            user_basecoin.changeTitleAndContent(getString(R.string.coinbase),baseUser.coinbase.toString())
            user_coin.changeTitleAndContent(getString(R.string.coin),baseUser.coin.toString())

        }

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

            false ->{

                val intent = Intent(this, CoinInfoActivity::class.java)

                startActivity(intent)

            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        coin_charge?.dispose()
    }

}
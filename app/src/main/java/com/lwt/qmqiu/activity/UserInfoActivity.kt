package com.lwt.qmqiu.activity



import android.os.Bundle
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.BarView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.mvp.contract.UserInfoContract
import com.lwt.qmqiu.mvp.present.UserInfoPresent
import com.lwt.qmqiu.network.ApiService
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_userinfo.*


class UserInfoActivity : BaseActivity(),BarView.BarOnClickListener, UserInfoContract.View {



    private lateinit var mUserName:String
    private lateinit var present: UserInfoPresent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_userinfo)

        //房间信息
        mUserName=intent.getStringExtra("name")

        userinfo_barview.setBarOnClickListener(this)
        userinfo_barview.changeTitle(getString(R.string.user_info))

        present = UserInfoPresent(this,this)

        present.userFind(mUserName,bindToLifecycle())

        userinfo_barview.showMore(SPHelper.getInstance().get("loginName","") as String == mUserName)
    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

            false -> {

                UiUtils.showToast("可以编辑")

            }


        }

    }
    override fun setUser(baseUser: BaseUser) {

        //图像
        Glide.with(this).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)


        user_name.changeTitleAndContent(baseUser.name,"")
        user_gender.changeTitleAndContent("性别",if(baseUser.male)"男" else "女")
        user_age.changeTitleAndContent("年龄",baseUser.age.toString())
        user_basecoin.changeTitleAndContent("青木",baseUser.coinbase.toString())
        user_coin.changeTitleAndContent("青木球",baseUser.coin.toString())


        Logger.e(baseUser.toString())

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        UiUtils.showToast(errMessage!!)

    }

}
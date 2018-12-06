package com.lwt.qmqiu.fragment

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.activity.*
import com.lwt.qmqiu.bean.GiftInfo
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.widget.ItemView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment(), View.OnClickListener, ItemView.ItemOnClickListener {



    override fun getLayoutResources(): Int {

        return  R.layout.fragment_mine
    }

    override fun initView() {

        setUser()

        user_img.setOnClickListener(this)

        itemview_room.setBarOnClickListener(this,R.id.itemview_room)
        itemview_gift.setBarOnClickListener(this,R.id.itemview_gift)
        itemview_charge.setBarOnClickListener(this,R.id.itemview_charge)
        itemview_exchange.setBarOnClickListener(this,R.id.itemview_exchange)
        itemview_setting.setBarOnClickListener(this,R.id.itemview_setting)
    }

    private fun setUser() {

        var baseUser = App.instanceApp().getLocalUser()

        if(baseUser!=null){

            //图像
            Glide.with(activity!!).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)

            user_name.changeTitleAndContent(baseUser.name,"")
            user_gender.changeTitleAndContent("性别",if(baseUser.male)"男" else "女")
            user_age.changeTitleAndContent("年龄",baseUser.age.toString())

        }

    }


    override fun onResume() {
        super.onResume()

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.user_img -> {

                var baseUser = App.instanceApp().getLocalUser()

                if(baseUser!=null){

                    val intent = Intent(activity, UserInfoActivity::class.java)

                    intent.putExtra("name",baseUser.name)

                    startActivity(intent)
                }

            }


        }
    }

    override fun itemViewClick(id: Int) {

        when (id) {

            R.id.itemview_room -> {

                val intent = Intent(activity, RoomSettingActivity::class.java)

                startActivity(intent)
            }

            R.id.itemview_gift -> {

                val intent = Intent(activity, GiftInfoActivity::class.java)

                startActivity(intent)

            }
            R.id.itemview_charge -> {

                val intent = Intent(activity, ChargeActivity::class.java)

                startActivity(intent)

            }
            R.id.itemview_exchange -> {

                val intent = Intent(activity, ExchangeActivity::class.java)

                startActivity(intent)

            }
            R.id.itemview_setting -> {

                val intent = Intent(activity, SettingActivity::class.java)

                startActivity(intent)

            }

        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setUser()
    }

}
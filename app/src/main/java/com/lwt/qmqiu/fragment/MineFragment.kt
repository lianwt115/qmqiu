package com.lwt.qmqiu.fragment

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.activity.*
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.widget.ItemView
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment(), View.OnClickListener, ItemView.ItemOnClickListener {



    override fun getLayoutResources(): Int {

        return  R.layout.fragment_mine
    }

    override fun initView() {

        user_img.setOnClickListener(this)

        itemview_room.setBarOnClickListener(this,R.id.itemview_room)
        itemview_note.setBarOnClickListener(this,R.id.itemview_note)
        itemview_gift.setBarOnClickListener(this,R.id.itemview_gift)
        itemview_charge.setBarOnClickListener(this,R.id.itemview_charge)
        itemview_exchange.setBarOnClickListener(this,R.id.itemview_exchange)
        itemview_setting.setBarOnClickListener(this,R.id.itemview_setting)
        itemview_cashout.setBarOnClickListener(this,R.id.itemview_cashout)
    }

    private fun setUser() {

        var baseUser = App.instanceApp().getLocalUser()

        if(baseUser!=null){

            //图像
            Glide.with(activity!!).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)

            //修改为showName
            user_name.changeTitleAndContent(baseUser.showName,"")
            user_gender.changeTitleAndContent(getString(R.string.user_gender),getString(if(baseUser.male)R.string.men else R.string.women))
            user_age.changeTitleAndContent(getString(R.string.user_age),baseUser.age.toString())

        }

    }


    override fun onResume() {
        super.onResume()

        setUser()
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.user_img -> {

                toUserInfoActivity(false)

            }


        }
    }

    private fun toUserInfoActivity(exchange: Boolean) {
        var baseUser = App.instanceApp().getLocalUser()

        if (baseUser != null) {

            val intent = Intent(activity, UserInfoActivity::class.java)

            intent.putExtra("name", baseUser.name)
            intent.putExtra("exchange", exchange)
            startActivity(intent)
        }
    }

    override fun itemViewClick(id: Int) {

        when (id) {

            R.id.itemview_room -> {

                val intent = Intent(activity, RoomSettingActivity::class.java)

                startActivity(intent)
            }
            R.id.itemview_note -> {

                val intent = Intent(activity, NoteSettingActivity::class.java)

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

                toUserInfoActivity(true)

            }
            R.id.itemview_cashout -> {

                val intent = Intent(activity, CashoutActivity::class.java)

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
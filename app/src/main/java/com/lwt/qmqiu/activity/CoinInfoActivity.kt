package com.lwt.qmqiu.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ChatAdatpter
import com.lwt.qmqiu.fragment.CoinListFragment
import com.lwt.qmqiu.fragment.GiftListFragment
import com.lwt.qmqiu.utils.StaticValues
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_coininfo.*


class CoinInfoActivity : BaseActivity(), BarView.BarOnClickListener, TabLayout.OnTabSelectedListener {



    private var mTabs = listOf<String>("充值", "消费","兑换","充值码").toMutableList()

    private lateinit var mFragments: ArrayList<androidx.fragment.app.Fragment>

    //0 充值 1消费
    private val STRATEGY = arrayOf(0,1,2,3)//接口路徑路由


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coininfo)

        coininfo_barview.setBarOnClickListener(this)
        coininfo_barview.changeTitle("货币流通记录")

        initFragment()
    }

    private fun initFragment() {

        mFragments = ArrayList()

        var incoomFragment: CoinListFragment = CoinListFragment()
        var incoomBundle = Bundle()
        incoomBundle.putInt("type",STRATEGY[0])
        incoomFragment.arguments = incoomBundle


        var payFragment: CoinListFragment = CoinListFragment()
        var payBundle = Bundle()
        payBundle.putInt("type", STRATEGY[1])
        payFragment.arguments = payBundle

        var exchangeFragment: CoinListFragment = CoinListFragment()
        var exchangeBundle = Bundle()
        exchangeBundle.putInt("type", STRATEGY[2])
        exchangeFragment.arguments = exchangeBundle


        mFragments.add(incoomFragment as androidx.fragment.app.Fragment)
        mFragments.add(payFragment as androidx.fragment.app.Fragment)
        mFragments.add(exchangeFragment as androidx.fragment.app.Fragment)





        var localUser = App.instanceApp().getLocalUser()

        if (localUser!=null && localUser.name== StaticValues.AdminName){

            var chargeFragment: CoinListFragment = CoinListFragment()
            var chargeBundle = Bundle()
            chargeBundle.putInt("type", STRATEGY[3])
            chargeFragment.arguments = chargeBundle

            mFragments.add(chargeFragment as androidx.fragment.app.Fragment)
        }


        vp_content.adapter = ChatAdatpter(supportFragmentManager, mFragments, mTabs)

        tabs.setupWithViewPager(vp_content)

        tabs.addOnTabSelectedListener(this)

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
    }


}

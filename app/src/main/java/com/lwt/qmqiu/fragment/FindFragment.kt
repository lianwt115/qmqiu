package com.lwt.qmqiu.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment


import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ChatAdatpter
import com.orhanobut.logger.Logger

import kotlinx.android.synthetic.main.fragment_find.*


/**
 * Created by Administrator on 2018\1\5 0005.
 */
class FindFragment : BaseFragment(), TabLayout.OnTabSelectedListener{

    val app= App.instanceApp()
    var mTabs = listOf<String>(app.getString(R.string.list1_name), app.getString(R.string.list2_name),
            app.getString(R.string.list3_name),app.getString(R.string.list4_name)).toMutableList()

    lateinit var mFragments: ArrayList<Fragment>

    val STRATEGY = arrayOf(2,1,3,4)//接口路徑路由

    var index=STRATEGY[0]

    override fun getLayoutResources(): Int {

        return R.layout.fragment_find

       }

    override fun initView() {

        var recommendFragment: ListFragment = ListFragment()
        var recommendBundle = Bundle()
        recommendBundle.putInt("type", STRATEGY[0])
        recommendFragment.arguments = recommendBundle


        var activeFragment: ListFragment = ListFragment()
        var activeBundle = Bundle()
        activeBundle.putInt("type", STRATEGY[1])
        activeFragment.arguments = activeBundle


        var adorableFragment: ListFragment = ListFragment()
        var adorableBundle = Bundle()
        adorableBundle.putInt("type", STRATEGY[2])
        adorableFragment.arguments = adorableBundle

        var freeFragment: ListFragment = ListFragment()
        var freeBundle = Bundle()
        freeBundle.putInt("type", STRATEGY[3])
        freeFragment.arguments = freeBundle


        mFragments = ArrayList()
        mFragments.add(recommendFragment as Fragment)
        mFragments.add(activeFragment as Fragment)
        mFragments.add(adorableFragment as Fragment)
        mFragments.add(freeFragment as Fragment)


        vp_content.adapter = ChatAdatpter(fragmentManager!!, mFragments, mTabs)

        tabs.setupWithViewPager(vp_content)

        tabs.addOnTabSelectedListener(this)

       }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

        var lable= "recommend"

        when (tab!!.text.toString()) {

            mTabs[0]-> {
                lable= "recommend"
                index=STRATEGY[0]

            }

            mTabs[1] -> {
                lable= "active"
                index=STRATEGY[1]

            }

            mTabs[2] -> {
                lable= "new"
                index=STRATEGY[2]

            }

            mTabs[3] -> {
                lable= "free"
                index=STRATEGY[3]

            }
        }

        Logger.e("chat_tab:$lable")

    }
}
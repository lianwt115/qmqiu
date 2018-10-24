package com.lwt.qmqiu.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment


import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ChatAdatpter
import com.orhanobut.logger.Logger

import kotlinx.android.synthetic.main.fragment_find.*



class FindFragment : BaseFragment(), TabLayout.OnTabSelectedListener{

    val app= App.instanceApp()
    var mTabs = listOf<String>(app.getString(R.string.list1_name), app.getString(R.string.list2_name),
            app.getString(R.string.list3_name)).toMutableList()

    lateinit var mFragments: ArrayList<Fragment>

    val STRATEGY = arrayOf(1,2,3)//接口路徑路由

    var index=STRATEGY[0]

    override fun getLayoutResources(): Int {

        return R.layout.fragment_find

       }

    override fun initView() {

        var nearFragment: ListFragment = ListFragment()
        var nearBundle = Bundle()
        nearBundle.putInt("type",STRATEGY[0])
        nearFragment.arguments = nearBundle


        var publicFragment: ListFragment = ListFragment()
        var publicBundle = Bundle()
        publicBundle.putInt("type", STRATEGY[1])
        publicFragment.arguments = publicBundle


        var myFragment: ListFragment = ListFragment()
        var myBundle = Bundle()
        myBundle.putInt("type",STRATEGY[2])
        myFragment.arguments = myBundle




        mFragments = ArrayList()
        mFragments.add(nearFragment as Fragment)
        mFragments.add(publicFragment as Fragment)
        mFragments.add(myFragment as Fragment)



        vp_content.adapter = ChatAdatpter(fragmentManager!!, mFragments, mTabs)

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
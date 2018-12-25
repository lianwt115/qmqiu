package com.lwt.qmqiu.fragment

import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.tabs.TabLayout

import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ChatAdatpter
import com.lwt.qmqiu.utils.UiUtils
import com.orhanobut.logger.Logger


import kotlinx.android.synthetic.main.fragment_find.*



class FindFragment : BaseFragment(), TabLayout.OnTabSelectedListener{

    val app= App.instanceApp()
    var mTabs = listOf<String>(app.getString(R.string.list1_name), app.getString(R.string.list2_name),
            app.getString(R.string.list3_name)).toMutableList()

    lateinit var mFragments: ArrayList<androidx.fragment.app.Fragment>

    val STRATEGY = arrayOf(1,2,3)//接口路徑路由

    private var selectIndex = 0

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
        mFragments.add(nearFragment as androidx.fragment.app.Fragment)
        mFragments.add(publicFragment as androidx.fragment.app.Fragment)
        mFragments.add(myFragment as androidx.fragment.app.Fragment)



        vp_content.adapter = ChatAdatpter(fragmentManager!!, mFragments, mTabs)

        tabs.setupWithViewPager(vp_content)

        tabs.addOnTabSelectedListener(this)

        //搜索到房间直接进入
        search_tv.setOnClickListener {
            if (TextUtils.isEmpty(search_et.text)){
                UiUtils.showToast(getString(R.string.please_input_roomname))
            }else{
                (mFragments[selectIndex] as ListFragment).searchRoom(search_et.text.toString())
            }
        }


       }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

        selectIndex = tab!!.position

        Logger.e(tab.text.toString())
        Logger.e("index:${tab.position}")

    }
}
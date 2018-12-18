package com.lwt.qmqiu.fragment

import android.os.Bundle
import android.text.TextUtils
import com.google.android.material.tabs.TabLayout
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ChatAdatpter
import com.lwt.qmqiu.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_note.*


class NoteFragment: BaseFragment(), TabLayout.OnTabSelectedListener {


    val app= App.instanceApp()
    var mTabs = listOf<String>(app.getString(R.string.list4_name), app.getString(R.string.list5_name),
            app.getString(R.string.list6_name)).toMutableList()

    lateinit var mFragments: ArrayList<androidx.fragment.app.Fragment>

    val STRATEGY = arrayOf(1,2,3)//接口路徑路由
    private var selectIndex = 0
    override fun getLayoutResources(): Int {

        return  R.layout.fragment_note
    }

    override fun initView() {

        var dayFragment: NoteListFragment = NoteListFragment()
        var dayBundle = Bundle()
        dayBundle.putInt("type",STRATEGY[0])
        dayFragment.arguments = dayBundle


        var secondFragment: NoteListFragment = NoteListFragment()
        var secondBundle = Bundle()
        secondBundle.putInt("type", STRATEGY[1])
        secondFragment.arguments = secondBundle


        var rentFragment: NoteListFragment = NoteListFragment()
        var rentBundle = Bundle()
        rentBundle.putInt("type",STRATEGY[2])
        rentFragment.arguments = rentBundle


        mFragments = ArrayList()
        mFragments.add(dayFragment as androidx.fragment.app.Fragment)
        mFragments.add(secondFragment as androidx.fragment.app.Fragment)
        mFragments.add(rentFragment as androidx.fragment.app.Fragment)


        vp_note.adapter = ChatAdatpter(fragmentManager!!, mFragments, mTabs)

        tabsNote.setupWithViewPager(vp_note)

        //搜索到帖子显示
        search_tv.setOnClickListener {
            if (TextUtils.isEmpty(search_et.text)){
                UiUtils.showToast("请输入帖子标签")
            }else{
                (mFragments[selectIndex] as NoteListFragment).show("搜索帖子:${search_et.text}")
                //UiUtils.showToast("搜索:${search_et.text}--$selectIndex")
            }
        }

        tabsNote.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        selectIndex = tab!!.position
    }


}
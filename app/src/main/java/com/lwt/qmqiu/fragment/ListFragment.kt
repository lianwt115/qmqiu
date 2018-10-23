package com.lwt.qmqiu.fragment

import com.lwt.qmqiu.R
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * Created by Administrator on 2018\1\5 0005.
 */
class ListFragment: BaseFragment() {

     var mStrategy: Int=0

    override fun getLayoutResources(): Int {

        return R.layout.fragment_list

    }

    override fun initView() {


        if (arguments != null) {
            mStrategy = arguments!!.getInt("type")

        }

        tv_nodata.text = mStrategy.toString()

    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}

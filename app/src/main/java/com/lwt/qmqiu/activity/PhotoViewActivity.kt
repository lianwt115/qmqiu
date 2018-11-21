package com.lwt.qmqiu.activity

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.PhotoViewPageAdapter
import com.lwt.qmqiu.bean.PhotoViewData
import kotlinx.android.synthetic.main.activity_photoview.*
import java.util.ArrayList

class PhotoViewActivity:BaseActivity(), PhotoViewPageAdapter.PhotoSingleClick {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_photoview)

        var list =intent.getParcelableArrayListExtra<PhotoViewData>("photoViewData")

        var index =intent.getIntExtra("index",0)

        view_pager.adapter = PhotoViewPageAdapter(this,list,this)

        view_pager.currentItem = getRealIndex(list,index)


    }

    override fun photoViewSingleClick() {

        finish()
    }


    private fun  getRealIndex(list: ArrayList<PhotoViewData>, realIndex: Int):Int{

        list.forEachIndexed { index, photoViewData ->


            if (photoViewData.position == realIndex)
                return index

        }

        return 0

    }

}
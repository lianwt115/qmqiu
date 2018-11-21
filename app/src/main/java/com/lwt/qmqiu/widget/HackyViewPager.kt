package com.lwt.qmqiu.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class HackyViewPager(context: Context,attrs: AttributeSet?): ViewPager(context,attrs) {


    constructor(context: Context):this(context,null)

    //将爸爸的touch直接传给儿子
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }

    }

}
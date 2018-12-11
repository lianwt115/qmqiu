package com.lwt.qmqiu.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R


/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class ShowListView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr), View.OnClickListener, View.OnLongClickListener {


    private var mShowMore  = false

    private var mTextViewContent: TextView? = null

    private var mTextViewTitle: TextView? = null

    private var mRoot: RelativeLayout? = null

    private var mLine: View? = null

    private var mShowListOnClickListener: ShowListOnClickListener? = null

    private var mViewId = -1



    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initAttrs(context, attrs)
        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    fun setBarOnClickListener(mBarOnClickListener: ShowListOnClickListener, id: Int) {
        this.mViewId = id
        this.mShowListOnClickListener = mBarOnClickListener
    }

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_showlist, null)

        //输入标题
        mRoot = view.findViewById(R.id.showlist_root) as RelativeLayout

        mTextViewContent = view.findViewById(R.id.content_tv) as TextView
        mTextViewTitle = view.findViewById(R.id.title_tv) as TextView
        mLine = view.findViewById(R.id.showlist_line) as View

        mLine?.visibility = if (mShowMore) View.VISIBLE else View.GONE

        mRoot?.setOnClickListener(this)
        mRoot?.setOnLongClickListener(this)

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.ShowListView)

        mShowMore = ta.getBoolean(R.styleable.ShowListView_show_line,true)
        ta.recycle()

    }

    override fun onClick(v: View?) {

        if (mShowListOnClickListener==null) {
            return
        }

        when (v?.id) {


            R.id.showlist_root  -> {

                mShowListOnClickListener!!.showListViewClick(false,mTextViewContent?.text.toString(),this.mViewId)

            }


        }


    }

    override fun onLongClick(v: View?): Boolean {
        if (mShowListOnClickListener==null) {
            return false
        }

        when (v?.id) {


            R.id.showlist_root  -> {

                mShowListOnClickListener!!.showListViewClick(true,mTextViewContent?.text.toString(),this.mViewId)

            }


        }

        return true
    }


    fun changeTitleAndContent(title: String,content:String?=null) {

        if (!TextUtils.isEmpty(content))
            mTextViewContent?.text = content

        mTextViewTitle?.text = title
    }


    interface ShowListOnClickListener {

        fun showListViewClick(long:Boolean,content:String,id: Int)

    }

}
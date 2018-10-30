package com.lwt.qmqiu.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R


/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class BarView(context: Context,attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr), View.OnClickListener {


    //private val mContext: Context


    private var mDrawableLeft: Drawable? = null

    private var mDrawableRight: Drawable? = null

    private var mTitle: String? = null
    private var mShowMore  = false

    private var mTextViewContentLeft: TextView? = null
    private var mImagViewLeft: ImageButton? = null
    private var mImagViewRight: ImageButton? = null

    private var mBarOnClickListener: BarOnClickListener? = null



    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initAttrs(context, attrs)
        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    fun setBarOnClickListener(mBarOnClickListener: BarOnClickListener) {
        this.mBarOnClickListener = mBarOnClickListener
    }

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_barview, null)

        //输入标题
        mTextViewContentLeft = view.findViewById(R.id.title_tv) as TextView

        mImagViewLeft = view.findViewById(R.id.back_iv) as ImageButton
        mImagViewRight = view.findViewById(R.id.more_iv) as ImageButton

        //初始化自定义属性

        if (mDrawableLeft != null)
            mImagViewLeft?.setImageDrawable(mDrawableLeft)
        if (mDrawableLeft != null)
            mImagViewRight?.setImageDrawable(mDrawableLeft)
        if (mTitle != null)
            mTextViewContentLeft?.text = mTitle

        mImagViewRight?.visibility = if (mShowMore) View.VISIBLE else View.GONE


        mImagViewLeft?.setOnClickListener(this)
        mImagViewRight?.setOnClickListener(this)


        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.BarView)


        mTitle = ta.getString(R.styleable.BarView_textcenter_barview)
        mDrawableLeft = ta.getDrawable(R.styleable.BarView_imgleft_barview)
        mDrawableRight = ta.getDrawable(R.styleable.BarView_imgright_barview)
        mShowMore = ta.getBoolean(R.styleable.BarView_show_more,false)
        ta.recycle()

    }

    override fun onClick(v: View?) {

        if (mBarOnClickListener==null) {
            return
        }

        when (v?.id) {


            R.id.back_iv  -> {

                mBarOnClickListener!!.barViewClick(true)

            }

            R.id.more_iv-> {
                mBarOnClickListener!!.barViewClick(false)
            }
        }


    }


    fun changeTitle(content: String) {

        mTextViewContentLeft?.text = content
    }

    fun showMore(show:Boolean){

        mImagViewRight?.visibility = if (show) View.VISIBLE else View.GONE

    }


    interface BarOnClickListener {

        fun barViewClick(left: Boolean)

    }

}
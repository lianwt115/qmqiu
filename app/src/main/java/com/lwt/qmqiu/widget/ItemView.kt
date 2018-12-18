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
 class ItemView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr), View.OnClickListener {


    //private val mContext: Context


    private var mDrawable: Drawable? = null
    private var mTitle: String? = null
    private var mShowLine  = false


    private lateinit var mTextViewName: TextView
    private lateinit var mTextViewContent: TextView
    private lateinit var mImageView: ImageView
    private lateinit var mLine: View
    private lateinit var mRootView: RelativeLayout




    private var mItemOnClickListener: ItemOnClickListener? = null
    private var mViewID = 0



    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initAttrs(context, attrs)
        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    fun setBarOnClickListener(itemOnClickListener: ItemOnClickListener,id:Int) {
        this.mViewID = id
        this.mItemOnClickListener = itemOnClickListener
    }

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_itemview, null)

        //输入标题
        mTextViewName = view.findViewById(R.id.show_name) as TextView
        mTextViewContent = view.findViewById(R.id.show_content) as TextView
        mImageView = view.findViewById(R.id.show_img) as ImageView
        mLine = view.findViewById(R.id.line) as View
        mRootView = view.findViewById(R.id.itemview_root) as RelativeLayout

       if (mDrawable!=null)
           mImageView.setImageDrawable(mDrawable)

       if (mTitle != null)
           mTextViewName.text = mTitle

       mLine.visibility = if (mShowLine) View.VISIBLE else View.GONE


        mRootView.setOnClickListener(this)

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.ItemView)


        mTitle = ta.getString(R.styleable.ItemView_show_name)

        mDrawable = ta.getDrawable(R.styleable.ItemView_show_img)

        mShowLine = ta.getBoolean(R.styleable.ItemView_show_bottom_line,false)

        ta.recycle()

    }

    override fun onClick(v: View?) {

        if (mItemOnClickListener==null) {
            return
        }

        when (v?.id) {


            R.id.itemview_root  -> {

                mItemOnClickListener!!.itemViewClick(mViewID)

            }

        }


    }

    fun setShowContent(content:String){

        mTextViewContent.text = content

        mTextViewContent.visibility = if (TextUtils.isEmpty(content)) View.GONE else View.VISIBLE


    }

    fun getShowContent():String{

        return mTextViewContent.text.toString()

    }

    interface ItemOnClickListener {

        fun itemViewClick(id: Int)

    }

}
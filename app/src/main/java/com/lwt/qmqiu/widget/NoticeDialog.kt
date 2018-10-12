package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.mipmap.tip
import com.lwt.qmqiu.utils.DeviceUtil


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class NoticeDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context)  {

        private var mLoginDialog: NoticeDialog? = null
        private var mLayout: View? = null
        private var mNoticeTVNotice: TextView? = null
        private var mBtLeft: Button? = null
        private var mBtRight: Button? = null
         var mListen: BtClickListen? = null
        private var index = 0

        fun create(notice:String): NoticeDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mLoginDialog = NoticeDialog(mContext, R.style.MyDialog)

            mLoginDialog!!.setCanceledOnTouchOutside(false)

            mLayout = inflater.inflate(R.layout.dialog_notice, null)

            mLoginDialog!!.addContentView(mLayout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 180f), DeviceUtil.dip2px(mContext, 240f)))


            mNoticeTVNotice = mLayout!!.findViewById(R.id.dialog_text) as TextView
            mBtLeft = mLayout!!.findViewById(R.id.bt_left) as Button
            mBtRight = mLayout!!.findViewById(R.id.bt_right) as Button

            mBtLeft!!.setOnClickListener({

                if (mListen !=null) {
                    mListen!!.btClick(true)
                }

            })
            mBtRight!!.setOnClickListener({

                if (mListen !=null) {
                    mListen!!.btClick(false)
                }

            })
            initView(notice)

            return mLoginDialog as NoticeDialog
        }

        fun initView(notice: String) {

            mNoticeTVNotice!!.text=notice


        }

        interface BtClickListen{

            fun  btClick(left:Boolean)
        }


    }
}
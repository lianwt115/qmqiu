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
import com.lwt.qmqiu.utils.DeviceUtil


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class NoticeDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context) : View.OnClickListener {

        private var mLoginDialog: NoticeDialog? = null
        private var mLayout: View? = null
        private var mNoticeImg: ImageView? = null
        private var mExitImg: ImageView? = null
        private var mNoticeTVTip: TextView? = null
        private var mNoticeTVNotice: TextView? = null
        private var mNoticeTVNoticeCenter: TextView? = null
        private var mNoticeTVNoticeBottom: TextView? = null
        private var mBtRoot: RelativeLayout? = null
        private var mBtLeft: Button? = null
        private var mBtRight: Button? = null
         var mListen: BtClickListen? = null
        private var index = 0

        fun create(tip:String,notice:String,img:Int= -1,showExit:Boolean,centerText:String?=null,showBt:Boolean=false): NoticeDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mLoginDialog = NoticeDialog(mContext, R.style.MyDialog)

            mLoginDialog!!.setCanceledOnTouchOutside(false)

            mLayout = inflater.inflate(R.layout.dialog_notice, null)

            mLoginDialog!!.addContentView(mLayout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 600f), DeviceUtil.dip2px(mContext, 480f)))


            mNoticeImg = mLayout!!.findViewById(R.id.dialog_img) as ImageView
            mExitImg = mLayout!!.findViewById(R.id.exit_img) as ImageView
            mNoticeTVTip = mLayout!!.findViewById(R.id.dialog_tip) as TextView
            mNoticeTVNotice = mLayout!!.findViewById(R.id.dialog_text) as TextView
            mNoticeTVNoticeCenter = mLayout!!.findViewById(R.id.dialog_text_center) as TextView
            mNoticeTVNoticeBottom = mLayout!!.findViewById(R.id.dialog_text_bottom) as TextView
            mBtRoot = mLayout!!.findViewById(R.id.bt_root) as RelativeLayout
            mBtLeft = mLayout!!.findViewById(R.id.bt_left) as Button
            mBtRight = mLayout!!.findViewById(R.id.bt_right) as Button

            mBtRoot!!.visibility=if (showBt) View.VISIBLE else View.GONE
            mExitImg!!.setOnClickListener(this)
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
            initView(tip,notice,img,showExit,centerText)

            return mLoginDialog as NoticeDialog
        }

        fun initView(tip: String, notice: String, img: Int, showExit: Boolean, centerText: String?) {

            mNoticeTVTip!!.text=tip

            mNoticeImg!!.visibility= if (img == -1) View.GONE else View.VISIBLE
            mNoticeTVNotice!!.visibility= if (img == -1) View.GONE else View.VISIBLE
            mNoticeTVNoticeCenter!!.visibility= if (img == -1) View.VISIBLE else View.GONE

            mExitImg!!.visibility= if (showExit) View.VISIBLE else View.GONE

            if (img == -1){

                mNoticeTVNoticeCenter!!.text = centerText

            }else{

                mNoticeTVNotice!!.text=notice

                Glide.with(mContext).asGif().load(img).into(mNoticeImg!!)
            }


        }

        fun setBottomText(text:String){

            mNoticeTVNoticeBottom!!.text = text

        }

        override fun onClick(v: View?) {

            mLoginDialog?.dismiss()
        }


        interface BtClickListen{

            fun  btClick(left:Boolean)
        }

        fun setShowBt(show:Boolean){
            mBtRoot!!.visibility=if (show) View.VISIBLE else View.GONE
        }


    }
}
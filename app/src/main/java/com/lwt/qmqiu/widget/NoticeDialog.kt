package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.bt_go
import com.lwt.qmqiu.R.mipmap.tip
import com.lwt.qmqiu.utils.DeviceUtil
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class NoticeDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context,private val cancle: Boolean) : DialogInterface.OnDismissListener {


        private var mLoginDialog: NoticeDialog? = null
        private var mLayout: View? = null
        private var mNoticeTVNotice: TextView? = null
        private var mRoomName: EditText? = null
        private var mLine: View? = null
        private var mType  = 0

        private var mBtNext: CircularProgressButton? = null
        private var mListen: BtClickListen? = null
        private var index = 0

        fun create(notice:String): NoticeDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mLoginDialog = NoticeDialog(mContext, R.style.MyDialog)

            mLoginDialog!!.setCanceledOnTouchOutside(cancle)
            mLoginDialog!!.setOnDismissListener(this)
            mLayout = inflater.inflate(R.layout.dialog_notice, null)

            mLoginDialog!!.addContentView(mLayout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 220f),LinearLayout.LayoutParams.WRAP_CONTENT ))


            mNoticeTVNotice = mLayout!!.findViewById(R.id.dialog_text) as TextView
            mRoomName = mLayout!!.findViewById(R.id.room_name) as EditText
            mBtNext = mLayout!!.findViewById(R.id.bt_go) as CircularProgressButton
            mLine = mLayout!!.findViewById(R.id.line) as View


            mBtNext?.setFinalCornerRadius(6F)
            mBtNext?.text = "NEXT"
            mBtNext?.background = mContext.getDrawable(R.drawable.bt_shape_2)

            mBtNext!!.setOnClickListener {

                if (mType == 3  && TextUtils.isEmpty(mRoomName!!.text.toString())){
                    UiUtils.showToast("内容不能为空")
                    return@setOnClickListener

                }

                if (mListen !=null) {

                    if (mListen!!.btClick(if (mType == 3) mRoomName!!.text.toString() else "")) {

                        mBtNext!!.startAnimation()
                    }
                }

            }

            initView(notice,cancle)

            return mLoginDialog as NoticeDialog
        }

        fun initView(notice: String,cancle: Boolean) {

            mNoticeTVNotice!!.text=notice

            mLoginDialog!!.setCanceledOnTouchOutside(cancle)
        }
        fun setListen(listen:BtClickListen?,type:Int){

            this.mListen = listen
            this.mType = type

            when (type) {
                //只要标题
                1 -> {
                    mBtNext!!.visibility = View.GONE

                    mLine!!.visibility = View.GONE

                    mRoomName!!.visibility =  View.GONE

                }
                //标题和按钮

                2 -> {
                    mBtNext!!.visibility = View.VISIBLE

                    mLine!!.visibility = View.VISIBLE
                    mRoomName!!.visibility =  View.GONE

                }
                //标题 按钮 和et
                3 -> {
                    mBtNext!!.visibility = View.VISIBLE

                    mLine!!.visibility = View.VISIBLE

                    mRoomName!!.visibility =  View.VISIBLE

                }

            }


        }

        fun btFinish(boolean: Boolean){

            if (mType == 2 || mType == 3)
                mBtNext!!.doneLoadingAnimation(mContext.resources.getColor(R.color.white),if (boolean) BitmapFactory.decodeResource(mContext.resources,R.mipmap.ic_done) else BitmapFactory.decodeResource(mContext.resources,R.mipmap.error))

            Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({
                mLoginDialog!!.dismiss()
            },{
                Logger.e("延迟关闭对话框异常")
            })
        }

        override fun onDismiss(dialog: DialogInterface?) {
            mRoomName?.setText("")
            mBtNext?.revertAnimation()
        }

        interface BtClickListen{

            fun  btClick(etContent:String):Boolean
        }


    }
}
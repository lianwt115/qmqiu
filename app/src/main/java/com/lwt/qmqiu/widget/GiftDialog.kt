package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.text.Spanned
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R
import com.opensource.svgaplayer.*
import com.orhanobut.logger.Logger
import org.jetbrains.annotations.NotNull
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth



/**
 * Created by Administrator on 2018\2\1 0001.
 */
class GiftDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}


    class Builder(private val mContext: Context) {


        private var mGiftDialog: GiftDialog? = null
        private var layout: View? = null
        private var mTvGiftInfo: TextView? = null
        private var mSVGAImageView: SVGAImageView? = null
        private  lateinit var mSVGAParser:SVGAParser

        fun create(): GiftDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mGiftDialog = GiftDialog(mContext, R.style.GiftDialog)
            mGiftDialog!!.setCanceledOnTouchOutside(false)
            layout = inflater.inflate(R.layout.dialog_giftnotice, null)

            val display = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            var point = Point()
            display.defaultDisplay.getSize(point)

            //对话框全屏显示
            mGiftDialog!!.addContentView(layout!!, ViewGroup.LayoutParams(
                    point.x , point.y))

            initView()

            return mGiftDialog!!
        }

        private fun initView() {

            mTvGiftInfo = layout!!.findViewById(R.id.gift_info) as TextView
            mSVGAImageView = layout!!.findViewById(R.id.svgaPlayer_dialog) as SVGAImageView

            mSVGAImageView!!.clearsAfterStop =true

            mSVGAImageView!!.loops = 1

            mSVGAParser = SVGAParser(mContext)


            mSVGAImageView!!.callback = object :SVGACallback{

                override fun onFinished() {

                    mGiftDialog!!.dismiss()

                }

                override fun onPause() {

                }

                override fun onRepeat() {

                }

                override fun onStep(frame: Int, percentage: Double) {

                }

            }
        }

        fun start(info: Spanned, path:String){

            mTvGiftInfo!!.text = info

            //播放动画
            mSVGAParser.parse(path, object : SVGAParser.ParseCompletion {
                override fun onComplete(@NotNull videoItem: SVGAVideoEntity) {
                    var drawable = SVGADrawable(videoItem)
                    mSVGAImageView!!.setImageDrawable(drawable)
                    mSVGAImageView!!.startAnimation()
                }

                override fun onError() {

                    Logger.e("动画异常")
                }
            })

        }



    }
}
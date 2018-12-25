package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ReportAdapter
import com.lwt.qmqiu.bean.ReportInfo
import com.lwt.qmqiu.utils.DeviceUtil
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class ReporterDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context,private val cancle: Boolean) : DialogInterface.OnDismissListener, ReportAdapter.ReportClickListen {

        private lateinit var mReporterDialog: ReporterDialog
        private lateinit var mLayout: View
        private lateinit var mTitle: TextView

        private lateinit var mReportAdapter: ReportAdapter

        private lateinit var mRecyclerView: androidx.recyclerview.widget.RecyclerView

        private  var mReporterList = ArrayList<ReportInfo>()

        private lateinit var mBtNext: CircularProgressButton
        private var mListen: BtClickListen? = null
        private var index = -1
        private var type = -1

        fun create(notice:String,listen:BtClickListen?,type: Int=0): ReporterDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mReporterDialog = ReporterDialog(mContext, R.style.MyDialog)

            mReporterDialog.setCanceledOnTouchOutside(cancle)
            mReporterDialog.setOnDismissListener(this)
            mLayout = inflater.inflate(R.layout.dialog_reporter, null)

            mReporterDialog.addContentView(mLayout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 300f),LinearLayout.LayoutParams.WRAP_CONTENT ))


            mTitle = mLayout.findViewById(R.id.title_report) as TextView


            mBtNext = mLayout.findViewById(R.id.report_go) as CircularProgressButton

            mRecyclerView = mLayout.findViewById(R.id.report_rv) as androidx.recyclerview.widget.RecyclerView

            this.type = type

            initRecycleView()

            mBtNext.setFinalCornerRadius(6F)
            mBtNext.text = "NEXT"
            mBtNext.background = mContext.getDrawable(R.drawable.bt_shape_2)

            mBtNext.setOnClickListener {


                if (mListen !=null) {

                    if (mListen!!.btClick(index,this.type)) {

                        mBtNext.startAnimation()
                    }
                }

            }

            initView(notice,cancle,listen)

            return mReporterDialog as ReporterDialog
        }


        private fun initRecycleView() {

            val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(mContext){
                override fun canScrollVertically(): Boolean {
                    return true
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            mRecyclerView.layoutManager=linearLayoutManager

            mReportAdapter= ReportAdapter(mContext,mReporterList,this)

            mRecyclerView.adapter = mReportAdapter

            mRecyclerView.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                    outRect.top = 0
                    outRect.bottom = 0
                    outRect.left = 0
                    outRect.right =0
                }
            })

            initData(this.type)

        }

         fun initData(type: Int,title:String="") {

             this.type  = type
             this.index = -1
            mReporterList.clear()

            when (this.type) {

                //选择
                0 -> {

                    mReporterList.add(ReportInfo("复制"))
                    mReporterList.add(ReportInfo("举报"))

                }

                //举报
                1 -> {

                    mReporterList.add(ReportInfo("恋童癖,儿童色情"))
                    mReporterList.add(ReportInfo("垃圾,灌水.钓鱼信息"))
                    mReporterList.add(ReportInfo("骚扰.变态.攻击信息"))
                    mReporterList.add(ReportInfo("个人数据"))
                    mReporterList.add(ReportInfo("轻度色情内容"))
                    mReporterList.add(ReportInfo("色情,生殖器官"))
                }
                //打开地图
                2 -> {

                    mReporterList.add(ReportInfo("百度地图"))
                    mReporterList.add(ReportInfo("高德地图"))
                    mReporterList.add(ReportInfo("腾讯地图"))

                }
            }

            mReportAdapter.notifyDataSetChanged()

            mBtNext.visibility =if (this.type == 1) View.VISIBLE else View.GONE

            mTitle.text = title
        }

        override fun reportClick(report: String, position: Int) {

            this.index = position

            if (mListen !=null && (this.type == 0 || this.type == 2)) {

                mListen!!.btClick(index,this.type)

            }


        }

        fun initView(notice: String,cancle: Boolean,listen:BtClickListen?) {

            this.mListen = listen

            mTitle.text = notice

            mReporterDialog.setCanceledOnTouchOutside(cancle)


        }

        fun btFinish(boolean: Boolean){

            mBtNext!!.doneLoadingAnimation(mContext.resources.getColor(R.color.white),if (boolean) BitmapFactory.decodeResource(mContext.resources,R.mipmap.ic_done) else BitmapFactory.decodeResource(mContext.resources,R.mipmap.error))

            Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({
                mReporterDialog.dismiss()
            },{
                Logger.e("延迟关闭对话框异常")
            })
        }

        override fun onDismiss(dialog: DialogInterface?) {
            mBtNext.revertAnimation()
        }

        interface BtClickListen{

            fun  btClick(index:Int,type: Int):Boolean
        }


    }
}
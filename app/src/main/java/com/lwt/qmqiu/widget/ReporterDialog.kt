package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.bt_go
import com.lwt.qmqiu.R.id.recycleview_im
import com.lwt.qmqiu.R.mipmap.tip
import com.lwt.qmqiu.adapter.IMListAdapter
import com.lwt.qmqiu.adapter.ReportAdapter
import com.lwt.qmqiu.bean.ReportInfo
import com.lwt.qmqiu.utils.DeviceUtil
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_im.*
import kotlinx.android.synthetic.main.activity_register.*
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

        private lateinit var mRecyclerView: RecyclerView

        private  var mReporterList = ArrayList<ReportInfo>()

        private lateinit var mBtNext: CircularProgressButton
        private var mListen: BtClickListen? = null
        private var index = -1

        fun create(notice:String,listen:BtClickListen?): ReporterDialog {

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

            mRecyclerView = mLayout.findViewById(R.id.report_rv) as RecyclerView

            initRecycleView()

            mBtNext.setFinalCornerRadius(6F)
            mBtNext.text = "NEXT"
            mBtNext.background = mContext.getDrawable(R.drawable.bt_shape_2)

            mBtNext.setOnClickListener {


                if (mListen !=null) {

                    if (mListen!!.btClick(index)) {

                        mBtNext.startAnimation()
                    }
                }

            }

            initView(notice,cancle,listen)

            return mReporterDialog as ReporterDialog
        }


        private fun initRecycleView() {

            val linearLayoutManager = object : LinearLayoutManager(mContext){
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

            mRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.top = 0
                    outRect.bottom = 0
                    outRect.left = 0
                    outRect.right =0
                }
            })

            initData()

        }

        private fun initData() {

            mReporterList.add(ReportInfo("恋童癖,儿童色情"))
            mReporterList.add(ReportInfo("垃圾,灌水.钓鱼信息"))
            mReporterList.add(ReportInfo("骚扰.变态.攻击信息"))
            mReporterList.add(ReportInfo("个人数据"))
            mReporterList.add(ReportInfo("轻度色情内容"))
            mReporterList.add(ReportInfo("色情,生殖器官"))

            mReportAdapter.notifyDataSetChanged()

        }

        override fun reportClick(report: String, position: Int) {

            this.index = position

        }

        fun initView(notice: String,cancle: Boolean,listen:BtClickListen?) {

            this.mListen = listen

            mTitle.text=notice

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

            fun  btClick(type:Int):Boolean
        }


    }
}
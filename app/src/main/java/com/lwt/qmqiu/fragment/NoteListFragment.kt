package com.lwt.qmqiu.fragment


import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.activity.*
import com.lwt.qmqiu.adapter.NoteListAdapter
import com.lwt.qmqiu.bean.NoteCommentLog
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.mvp.contract.NoteContract
import com.lwt.qmqiu.mvp.present.NotePresent
import com.lwt.qmqiu.shareelement.ShareContentInfo
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.StaticValues.Companion.mLocalUserName
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.CommentDialog
import com.lwt.qmqiu.widget.ReporterDialog
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_notelist.*

class NoteListFragment:BaseFragment(), OnRefreshListener, View.OnClickListener, NoteContract.View, NoteListAdapter.NoteClickListen, IShareElements, CommentDialog.Builder.CommentClickListen {



    override fun commentClick(type: Int, id: String, data: Any?, position: Int) {

        when (type) {
            0 -> {
                mCommentDialog.dismiss()
            }
            1 -> {
                mPresenter.createComment(mLocalUserName,id,data as String,bindToLifecycle())
            }

            //举报
            2 -> {

            }
            //删除
            3 -> {

                mPresenter.deleteComment(mLocalUserName,data as String,position,bindToLifecycle())
            }
        }

    }

    override fun setDeleteComment(success: Boolean, position: Int) {

        if (success)
            mCommentDialogBuilder.deleteData(position)
    }

    override fun setCreateComment(noteCommentLog: NoteCommentLog) {

        Logger.e(noteCommentLog.toString())
        mCommentDialogBuilder.addData(noteCommentLog)
    }

    override fun setGetComment(commentList: List<NoteCommentLog>, id: String) {

        mCommentDialogBuilder = CommentDialog.Builder(activity!!,false)

        mCommentDialog = mCommentDialogBuilder.create(commentList,this,id)

        mCommentDialog.show()

        Logger.e(commentList.toString())
    }


    override fun setReportNote(success: Boolean) {

        UiUtils.showToast(if (success )"举报成功" else "已经举报过了")

    }

    override fun setGoodNote(success: Boolean, position: Int) {

        if (success && position<mList.size){

            mList[position].goodNum++


        }else{

            UiUtils.showToast("已经点过赞了")

        }

        mList[position].hasGood = true
        mAdapter.notifyItemChanged(position)

    }

    override fun getShareElements(): Array<ShareElementInfo<PhotoViewData>> {

        return  arrayOf(ShareContentInfo(this.mSeleteView!!,this.mSeleteData))

    }
    private lateinit var mReporterDialogBuilder: ReporterDialog.Builder
    private lateinit var mReporterDialog: ReporterDialog

    private lateinit var mCommentDialogBuilder: CommentDialog.Builder
    private lateinit var mCommentDialog: CommentDialog
    private var mStrategy: Int=0
    private lateinit var mPresenter: NotePresent
    private lateinit var mAdapter: NoteListAdapter
    private var mList: ArrayList<NoteLog> = ArrayList()
    private  var mSeleteView:View? = null
    private  var mSeleteData: PhotoViewData? = null
    override fun getLayoutResources(): Int {

        return  R.layout.fragment_notelist

    }

    override fun initView() {

        if (arguments != null) {
            mStrategy = arguments!!.getInt("type")
            mPresenter= NotePresent(context!!,this)
        }

        //初始化list
        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(activity){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        recyclerView.layoutManager=linearLayoutManager

        mAdapter= NoteListAdapter(context!!,mList,this)

        recyclerView.adapter=mAdapter

        recyclerView.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 2
                outRect.bottom = 1
                outRect.left = 2
                outRect.right = 0
            }
        })


        smartrefreshlayout.isEnableLoadmore =false
        //刷新和加载更多
        smartrefreshlayout.setOnRefreshListener(this)

        var head= MaterialHeader(context)

        var foot= BallPulseFooter(context!!).setSpinnerStyle(SpinnerStyle.Scale)

        head.setColorSchemeColors(resources.getColor(R.color.colorAccent), resources.getColor(R.color.bg_tv_money), resources.getColor(R.color.colorAccent),resources.getColor(R.color.bg_tv_money),resources.getColor(R.color.colorAccent), resources.getColor(R.color.bg_tv_money))

        foot.setAnimatingColor(resources.getColor(R.color.colorAccent))

        smartrefreshlayout.refreshHeader=head

        smartrefreshlayout.refreshFooter=foot

        getData(null)

        smartrefreshlayout.autoRefresh()

        note_fab.setOnClickListener(this)

    }

    private fun getData(refreshlayout: RefreshLayout?) {

        val name = SPHelper.getInstance().get("loginName","") as String

        val location = App.instanceApp().getBDLocation()

        if (!TextUtils.isEmpty(name) && location!=null) {

            mPresenter.getNote(name, mStrategy, location.latitude, location.longitude, bindToLifecycle())

        } else {

            if (refreshlayout == null) {
                smartrefreshlayout.finishRefresh()
            }else{

                refreshlayout?.finishRefresh(1000)
            }

        }
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {

        getData(refreshlayout)

    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    fun show(text:String){

        UiUtils.showToast(text.plus("---$mStrategy"))

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.note_fab -> {

                val intent = Intent(activity, CreateNoteActivity::class.java)
                //那个类型的帖子
                intent.putExtra("type",mStrategy)
                startActivity(intent)
            }

        }
    }

    override fun noteClick(noteLog: NoteLog, position: Int, type: Int, view: View?) {

        when (type) {

            //头像,昵称点击
            0,1-> {

                val intent = Intent(activity, UserInfoActivity::class.java)

                intent.putExtra("name",noteLog.name)
                intent.putExtra("exchange",false)

                startActivity(intent)

            }

            //图片或视频点击
            2-> {
                //判断是图片还是视频

                when (noteLog.topic) {

                    //图片
                    "1" -> {

                        //转场动画所需
                        this.mSeleteData = PhotoViewData(position,noteLog.imgList)
                        this.mSeleteView = view

                        val intent = Intent(activity, PhotoViewActivity::class.java)

                        intent.putParcelableArrayListExtra("photoViewData", arrayListOf(this.mSeleteData))
                        intent.putExtra("index",0)
                        intent.putExtra("real",false)


                        val options = YcShareElement.buildOptionsBundle(activity!!, this)

                        startActivityForResult(intent, IMActivity.REQUEST_CONTENT, options)

                    }



                    //视频
                    "2" -> {

                        //转场动画所需
                        this.mSeleteData = PhotoViewData(position,noteLog.imgList)
                        this.mSeleteView = view

                        val intent = Intent(activity, VideoPlayActivity::class.java)

                        intent.putExtra("photoViewData",this.mSeleteData)

                        val options = YcShareElement.buildOptionsBundle(activity!!, this)

                        startActivityForResult(intent, IMActivity.REQUEST_CONTENT, options)

                    }
                }

            }
            //评论
            3-> {

                mPresenter.getComment(mLocalUserName,noteLog._id!!,bindToLifecycle())


            }
            //点赞
            4-> {

                if (!noteLog.hasGood)
                    mPresenter.goodNote(mLocalUserName,noteLog._id!!,position,bindToLifecycle())
                else
                    UiUtils.showToast("已经点过赞了")
            }
            //举报
            5-> {

                mReporterDialogBuilder =  ReporterDialog.Builder(activity!!,true)

                mReporterDialog = mReporterDialogBuilder.create("举报",object :ReporterDialog.Builder.BtClickListen{

                    override fun btClick(index: Int, type: Int): Boolean {

                        when (type) {

                            1 -> {

                                mPresenter.reportNote(mLocalUserName,noteLog._id!!,index,bindToLifecycle())

                                mReporterDialog.dismiss()

                                return true
                            }

                        }

                        return false
                    }

                },1)

                mReporterDialog.show()
            }
        }

        Logger.e("type:$type")
    }

    override fun setNote(noteList: List<NoteLog>) {

        mList.clear()

        mList.addAll(noteList)

        mList.sortBy {

            -it.creatTime
        }

        mAdapter.notifyDataSetChanged()

        smartrefreshlayout?.finishRefresh()
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {
            //帖子获取失败
            1 -> {

            }

            2 -> {

            }
        }
        UiUtils.showToast(errMessage?:"系统错误")
    }
}
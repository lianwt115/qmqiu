package com.lwt.qmqiu.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.activity.CreateNoteActivity
import com.lwt.qmqiu.activity.IMActivity
import com.lwt.qmqiu.activity.PhotoViewActivity
import com.lwt.qmqiu.activity.UserInfoActivity
import com.lwt.qmqiu.adapter.NoteListAdapter
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.mvp.contract.NoteContract
import com.lwt.qmqiu.mvp.present.NotePresent
import com.lwt.qmqiu.shareelement.ShareContentInfo
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_im.*
import kotlinx.android.synthetic.main.fragment_notelist.*

class NoteListFragment:BaseFragment(), OnRefreshListener, View.OnClickListener, NoteContract.View, NoteListAdapter.NoteClickListen, IShareElements {


    override fun getShareElements(): Array<ShareElementInfo<PhotoViewData>> {

        return  arrayOf(ShareContentInfo(this.mSeleteView!!,this.mSeleteData))

    }

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

            0,1-> {

                val intent = Intent(activity, UserInfoActivity::class.java)

                intent.putExtra("name",noteLog.name)
                intent.putExtra("exchange",false)

                startActivity(intent)

            }

            2-> {
                //转场动画所需
                this.mSeleteData = PhotoViewData(position,noteLog.nameImg)
                this.mSeleteView = view

                val intent = Intent(activity, PhotoViewActivity::class.java)

                intent.putParcelableArrayListExtra("photoViewData", arrayListOf(this.mSeleteData))
                intent.putExtra("index",0)
                intent.putExtra("real",false)


                val options = YcShareElement.buildOptionsBundle(activity!!, this)

                startActivityForResult(intent, IMActivity.REQUEST_CONTENT, options)

            }
            3-> {
            }
            4-> {
            }
            5-> {
            }
        }

        Logger.e("type:$type")
    }

    override fun setNote(noteList: List<NoteLog>) {

        mList.clear()

        mList.addAll(noteList)

        mAdapter.notifyDataSetChanged()

        smartrefreshlayout?.finishRefresh()
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {
            //帖子获取失败
            1 -> {

            }
            else -> {
            }
        }
        UiUtils.showToast(errMessage?:"系统错误")
    }
}
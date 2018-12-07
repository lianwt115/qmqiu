package com.lwt.qmqiu.fragment


import android.graphics.Rect
import android.view.View
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.GiftListAdapter
import com.lwt.qmqiu.adapter.IMChatRoomListAdapter
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.mvp.contract.GiftLogContract
import com.lwt.qmqiu.mvp.present.GiftLogPresent
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_giftlist.*


class GiftListFragment: BaseFragment(), GiftLogContract.View, OnRefreshListener{



    private var mStrategy: Int=0
    private lateinit var mPresenter: GiftLogPresent
    private lateinit var mAdapter: GiftListAdapter
    private var mList: ArrayList<GiftLog> = ArrayList()

    override fun getLayoutResources(): Int {

        return R.layout.fragment_giftlist

    }

    override fun initView() {


        if (arguments != null) {
            mStrategy = arguments!!.getInt("type")
            mPresenter = GiftLogPresent(activity!!,this)
        }


        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(activity){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        recyclerView.layoutManager=linearLayoutManager

        mAdapter= GiftListAdapter(context!!,mList,mStrategy)

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


        smartrefreshlayout.autoRefresh()



    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {

        getData()
    }


    override fun setGiftLog(giftLog: List<GiftLog>) {

        mList.clear()

        mList.addAll(giftLog)
        //倒序排列
        mList.sortBy {

            -it.happenTime

        }
        mAdapter.notifyDataSetChanged()

        if (mList.isEmpty())
            showProgressDialog("您还没有相关记录")

        smartrefreshlayout.finishRefresh()
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        Logger.e("${if (type ==1)"收入" else "支出"}接口异常${errMessage?:"错误消息为空"}")
        smartrefreshlayout.finishRefresh()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        var localUaser = App.instanceApp().getLocalUser()

        if (localUaser != null)
            mPresenter.getGiftLog(localUaser.name, mStrategy, bindToLifecycle())
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}

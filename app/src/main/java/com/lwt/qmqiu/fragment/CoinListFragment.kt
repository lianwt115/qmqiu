package com.lwt.qmqiu.fragment


import android.graphics.Rect
import android.view.View
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.CoinListAdapter
import com.lwt.qmqiu.adapter.GiftListAdapter
import com.lwt.qmqiu.bean.CoinLog
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.mvp.contract.CoinLogContract
import com.lwt.qmqiu.mvp.present.CoinLogPresent
import com.lwt.qmqiu.mvp.present.GiftLogPresent
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_coinlist.*


class CoinListFragment: BaseFragment(), CoinLogContract.View, OnRefreshListener {


    override fun setCoinRecord(giftLog: List<CoinLog>) {
        mList.clear()

        mList.addAll(giftLog)

        mList.sortBy {

            if (mStrategy==0)
                -it.chargeTime
            else
                -it.happenTime
        }

        mAdapter.notifyDataSetChanged()

        if (mList.isEmpty())
            showProgressDialog("您还没有相关记录")

        smartrefreshlayout.finishRefresh()
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        Logger.e(errMessage?:"错误消息为空")
        smartrefreshlayout.finishRefresh()
    }


    private var mStrategy: Int=0
    private lateinit var mPresenter: CoinLogPresent
    private lateinit var mAdapter: CoinListAdapter
    private var mList: ArrayList<CoinLog> = ArrayList()

    override fun getLayoutResources(): Int {

        return R.layout.fragment_coinlist

    }

    override fun initView() {

        if (arguments != null) {
            mStrategy = arguments!!.getInt("type")
            mPresenter = CoinLogPresent(activity!!,this)
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

        mAdapter= CoinListAdapter(context!!,mList,mStrategy)

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

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        var localUaser = App.instanceApp().getLocalUser()

        if (localUaser != null)
            mPresenter.coinRecord(localUaser.name, if (mStrategy == 1) 1 else 0,mStrategy != 0, bindToLifecycle())
    }


}

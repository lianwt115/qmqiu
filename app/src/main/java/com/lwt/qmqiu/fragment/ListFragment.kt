package com.lwt.qmqiu.fragment

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.IMChatRoomListAdapter
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.VideoSurface
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.IMChatRoomContract
import com.lwt.qmqiu.mvp.present.IMChatRoomPresent
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_list.*



class ListFragment: BaseFragment(), OnRefreshListener, OnLoadmoreListener, IMChatRoomContract.View, IMChatRoomListAdapter.RoomClickListen {



    private var mDisposable: Disposable? = null

    private lateinit var mPresenter: IMChatRoomPresent
    private var mStrategy: Int=0
    lateinit var mAdapter: IMChatRoomListAdapter
    var mList: ArrayList<IMChatRoom> = ArrayList()

    override fun getLayoutResources(): Int {

        return R.layout.fragment_list

    }

    override fun initView() {

        val linearLayoutManager = object : LinearLayoutManager(activity){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        recyclerView.layoutManager=linearLayoutManager

        mAdapter= IMChatRoomListAdapter(context!!,mList,this)

        recyclerView.adapter=mAdapter

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 2
                outRect.bottom = 1
                outRect.left = 2
                outRect.right = 0
            }
        })

        if (arguments != null) {
            mStrategy = arguments!!.getInt("type")
            mPresenter= IMChatRoomPresent(context!!,this)
        }


        //刷新和加载更多
        smartrefreshlayout.setOnRefreshListener(this)
        smartrefreshlayout.setOnLoadmoreListener(this)

        var head= MaterialHeader(context)

        var foot= BallPulseFooter(context!!).setSpinnerStyle(SpinnerStyle.Scale)

        head.setColorSchemeColors(-0x59ce01, -0x7c4d10, -0x59ce01, -0x7c4d10,-0x59ce01, -0x7c4d10)

        foot.setAnimatingColor(-0x59ce01)

        smartrefreshlayout.refreshHeader=head

        smartrefreshlayout.refreshFooter=foot

        getData(null)
        smartrefreshlayout.autoRefresh()


    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {

        refreshlayout?.finishLoadmore(1000)


    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {

        getData(refreshlayout)

    }

    private fun getData(refreshlayout: RefreshLayout?) {

        val name = SPHelper.getInstance().get("loginName","") as String
        var location = App.instanceApp().getBDLocation()

        if (!TextUtils.isEmpty(name)) {

            mPresenter.getIMChatRoom(name, location?.latitude ?: 0.000000, location?.longitude
                    ?: 0.000000, mStrategy, bindToLifecycle())

        } else {

            if (refreshlayout == null) {
                smartrefreshlayout.finishRefresh()
            }else{

                refreshlayout?.finishRefresh(1000)
            }

        }
    }

    override fun setIMChatRoom(roomList: List<IMChatRoom>) {

        mList.clear()
        mList.addAll(roomList)
        mAdapter.notifyDataSetChanged()

        smartrefreshlayout.finishRefresh()

    }

    override fun err(code: Int, errMessage: String?) {

        smartrefreshlayout.finishRefresh()
        UiUtils.showToast(errMessage!!)
    }

    override fun roomClick(content: IMChatRoom, position: Int) {

        UiUtils.showToast(content.creatName.plus(position))
    }



    override fun onDestroyView() {
        super.onDestroyView()

    }
}

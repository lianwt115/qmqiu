package com.lwt.qmqiu.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Base64
import android.view.View
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.baidu.location.BDLocation
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.color.bg_tv_money
import com.lwt.qmqiu.activity.IMActivity
import com.lwt.qmqiu.adapter.IMChatRoomListAdapter
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.VideoSurface
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.IMChatRoomContract
import com.lwt.qmqiu.mvp.present.IMChatRoomPresent
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.NoticeDialog
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.concurrent.TimeUnit



class ListFragment: BaseFragment(), OnRefreshListener, OnLoadmoreListener, IMChatRoomContract.View, IMChatRoomListAdapter.RoomClickListen, View.OnClickListener, NoticeDialog.Builder.BtClickListen {



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

        head.setColorSchemeColors(resources.getColor(R.color.colorAccent), resources.getColor(R.color.bg_tv_money), resources.getColor(R.color.colorAccent),resources.getColor(R.color.bg_tv_money),resources.getColor(R.color.colorAccent), resources.getColor(R.color.bg_tv_money))

        foot.setAnimatingColor(resources.getColor(R.color.colorAccent))

        smartrefreshlayout.refreshHeader=head

        smartrefreshlayout.refreshFooter=foot

        getData(null)
        smartrefreshlayout.autoRefresh()

        room_fab.setOnClickListener(this)


        if (mStrategy == 3)
            room_fab.hide()
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {

        refreshlayout?.finishLoadmore(1000)


    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {

        getData(refreshlayout)

    }

    override fun onResume() {
        super.onResume()
        getData(null)
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

        var user =App.instanceApp().getLocalUser()
        if (user != null){

            for (room in roomList) {

                room.lastContent = String(RSAUtils.decryptData(Base64.decode(room.lastContent,0), RSAUtils.loadPrivateKey(user.privateKey))!!)
            }
        }else{
            UiUtils.showToast(getString(R.string.see_clear))
        }

        mList.clear()
        mList.addAll(roomList)
        mAdapter.notifyDataSetChanged()

        smartrefreshlayout.finishRefresh()

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {

            //获取房间失败
            1 -> {
                smartrefreshlayout.finishRefresh()

                showProgressDialog(getString(R.string.roomget_err))
            }
            //创建房间失败
            2 -> {
                showProgressDialogSuccess(false)
            }
        }

        UiUtils.showToast(errMessage!!)
    }

    //点击进入房间
    override fun roomClick(imChatRoom: IMChatRoom, position: Int) {

        val intent = Intent(activity, IMActivity::class.java)

        intent.putExtra("imChatRoom",imChatRoom)

        startActivity(intent)
    }



    override fun onDestroyView() {
        super.onDestroyView()

    }

    /**
     * 创建成功后是直接进入还是刷新
     */
    override fun creatIMChatRoomSuccess(room: IMChatRoom) {

        getData(null)
        smartrefreshlayout.autoRefresh()

        showProgressDialogSuccess(true)

        Logger.e(room.toString())
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.room_fab -> {

                showProgressDialog("创建聊天室",true,3,this)

            }

        }
    }

    override fun btClick( etContent: String): Boolean {

        var user = App.instanceApp().getLocalUser()
        var location = App.instanceApp().getBDLocation()

        if (user != null){

            mPresenter.creatIMChatRoom(user.name,location?.latitude ?: 0.000000, location?.longitude
                    ?: 0.000000, mStrategy,etContent,bindToLifecycle())

            return  true

        }else{

            UiUtils.showToast("请先登录")

        }

        return  false

    }
}

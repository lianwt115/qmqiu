package com.lwt.qmqiu.fragment

import android.view.View
import com.lwt.qmqiu.R
import com.lwt.qmqiu.mvp.present.IMChatRoomPresent
import com.lwt.qmqiu.utils.UiUtils
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_notelist.*

class NoteListFragment:BaseFragment(), OnRefreshListener, View.OnClickListener {

    private var mStrategy: Int=0
    override fun getLayoutResources(): Int {

        return  R.layout.fragment_notelist

    }

    override fun initView() {

        if (arguments != null) {
            mStrategy = arguments!!.getInt("type")
        }

        //初始化list


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

        note_fab.setOnClickListener(this)

    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {

    }

    fun show(text:String){
        UiUtils.showToast(text.plus("---$mStrategy"))
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.note_fab -> {

                UiUtils.showToast("发布帖子$mStrategy")
            }

        }
    }
}
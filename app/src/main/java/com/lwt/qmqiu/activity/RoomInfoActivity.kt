package com.lwt.qmqiu.activity


import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.RoomUserAdapter
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.mvp.contract.RoomInfoContract
import com.lwt.qmqiu.mvp.present.RoomInfoPresent
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.ShowListView
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_roominfo.*
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import android.content.Intent
import android.text.ClipboardManager


class RoomInfoActivity : BaseActivity(),BarView.BarOnClickListener, RoomUserAdapter.UserClickListen, RoomInfoContract.View, View.OnClickListener {



    override fun setExitAndDelete(success: Boolean) {

        when (success) {

            true -> {

                UiUtils.showToast(getString(R.string.handle_success))

                Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

                    val intent = Intent()
                    //把返回数据存入Intent
                    intent.putExtra("exitanddelete", true)
                    //设置返回数据
                    this@RoomInfoActivity.setResult(Activity.RESULT_OK, intent)

                    finish()

                },{
                    Logger.e(getString(R.string.handle_err))
                })

            }
            false -> {
                UiUtils.showToast(getString(R.string.handle_err))
            }
        }

        room_exit!!.doneLoadingAnimation(resources.getColor(R.color.white),if (success) BitmapFactory.decodeResource(resources,R.mipmap.ic_done) else BitmapFactory.decodeResource(resources,R.mipmap.error))

    }

    override fun setActiveUser(activeList: List<BaseUser>) {

        mRoomUserList.addAll(activeList)

        mRoomUserAdapter.notifyDataSetChanged()

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        UiUtils.showToast(errMessage!!)

        when (type) {

            1 -> {

            }

            2 -> {

                room_exit.revertAnimation()

            }
        }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private lateinit var mIMChatRoom:IMChatRoom
    private lateinit var mRoomUserAdapter:RoomUserAdapter
    private  var mRoomUserList = ArrayList<BaseUser>()
    private lateinit var present: RoomInfoPresent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_roominfo)

        initRecycleView()

        //房间信息
        mIMChatRoom=intent.getParcelableExtra<IMChatRoom>("imChatRoom")


        roominfo_barview.setBarOnClickListener(this)
        roominfo_barview.changeTitle(mIMChatRoom.roomName)
        present = RoomInfoPresent(this,this)

        present.getActiveUser(if (App.instanceApp().isLogin())App.instanceApp().getLocalUser()?.name!! else SPHelper.getInstance().get("loginName","") as String,mIMChatRoom.roomNumber,bindToLifecycle())

        room_name.changeTitleAndContent("群组名称",mIMChatRoom.roomName)
        room_number.changeTitleAndContent("群组代码",mIMChatRoom.roomNumber)
        room_time.changeTitleAndContent("创建时间",formatter.format(mIMChatRoom.creatTime))
        room_creat.changeTitleAndContent("创建人",mIMChatRoom.creatName)

        room_number.setBarOnClickListener(object :ShowListView.ShowListOnClickListener{
            override fun showListViewClick(long: Boolean, content: String) {
                when (long) {

                    true -> {

                        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        // 将文本内容放到系统剪贴板里。
                        cm.text = content

                        UiUtils.showToast("群组代码复制成功",false)
                    }

                    false -> {

                        UiUtils.showToast("点击")

                    }
                }
            }

        })

        room_exit.setFinalCornerRadius(20f)

        room_exit.text = "删除并退出"
        room_exit.background =getDrawable(R.drawable.bg_20dp_16)
        room_exit.setOnClickListener(this)



    }
    private fun initRecycleView() {


        val gridLayoutManager = object : GridLayoutManager(this,5){

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        recycleview_roomuser.layoutManager=gridLayoutManager

        mRoomUserAdapter= RoomUserAdapter(this,mRoomUserList,this)

        recycleview_roomuser.adapter = mRoomUserAdapter

        recycleview_roomuser.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })
    }

    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

    //活跃用户点击
    override fun userClick(user: BaseUser, position: Int) {

        val intent = Intent(this, UserInfoActivity::class.java)

        intent.putExtra("name",user.name)

        startActivity(intent)

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.room_exit-> {

                room_exit.startAnimation()

                present.exitAndDelete(if (App.instanceApp().isLogin())App.instanceApp().getLocalUser()?.name!! else SPHelper.getInstance().get("loginName","") as String,mIMChatRoom.roomNumber,bindToLifecycle())

            }


        }
    }


}
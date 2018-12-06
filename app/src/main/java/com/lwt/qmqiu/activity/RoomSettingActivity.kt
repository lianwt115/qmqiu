package com.lwt.qmqiu.activity



import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.IMChatRoomListAdapter
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.mvp.contract.RoomSettingContract
import com.lwt.qmqiu.mvp.present.RoomSettingPresent
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.widget.BarView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_roomsetting.*



class RoomSettingActivity : BaseActivity(),BarView.BarOnClickListener, RoomSettingContract.View, IMChatRoomListAdapter.RoomClickListen {

    private lateinit var present: RoomSettingPresent
    private  var mIMChatRoomList = ArrayList<IMChatRoom>()
    private lateinit var mAdapter: IMChatRoomListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_roomsetting)

        present = RoomSettingPresent(this,this)

        roomsetting_barview.setBarOnClickListener(this)
        roomsetting_barview.changeTitle("我创建的")

        initRecycleView()
    }

    private fun initRecycleView() {


        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this){

        }
        recycleview_roomsetting.layoutManager=linearLayoutManager

        mAdapter= IMChatRoomListAdapter(this,mIMChatRoomList,this,4)

        recycleview_roomsetting.adapter = mAdapter

        recycleview_roomsetting.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
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

    override fun roomClick(content: IMChatRoom, position: Int) {

        val intent = Intent(this, RoomInfoActivity::class.java)

        intent.putExtra("imChatRoom",content)

        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        var localUser = App.instanceApp().getLocalUser()

        if (localUser != null)
            present.getRoomCreatByMe(localUser.name,bindToLifecycle())
    }

    override fun setRoomCreatByMe(roomList: List<IMChatRoom>) {

        var localUser = App.instanceApp().getLocalUser()

        if (localUser != null){

            for (room in roomList) {

                room.lastContent = String(RSAUtils.decryptData(Base64.decode(room.lastContent,0), RSAUtils.loadPrivateKey(localUser.privateKey))!!)
            }
        }


        mIMChatRoomList.clear()

        mIMChatRoomList.addAll(roomList)

        mAdapter.notifyDataSetChanged()

        if (roomList.isEmpty())

            showProgressDialog("您还没有创建房间")
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        Logger.e(errMessage?:"错误为空")
    }



}
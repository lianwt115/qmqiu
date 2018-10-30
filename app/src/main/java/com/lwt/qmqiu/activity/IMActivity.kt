package com.lwt.qmqiu.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Base64
import android.view.View
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.IMListAdapter
import com.lwt.qmqiu.adapter.VideoListAdapter
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.orhanobut.logger.Logger
import com.lwt.qmqiu.bean.VideoSurface
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.RoomMessageContract
import com.lwt.qmqiu.mvp.present.RoomMessagePresent
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.BarView
import kotlinx.android.synthetic.main.activity_im.*


class IMActivity : BaseActivity(), View.OnClickListener, IMListAdapter.IMClickListen, QMWebsocket.QMMessageListen, BarView.BarOnClickListener, RoomMessageContract.View {



    private lateinit var mIMChatRoom:IMChatRoom
    private lateinit var mVideoListAdapter:VideoListAdapter
    private lateinit var mIMListAdapter:IMListAdapter
    private lateinit var mWebSocket: QMWebsocket
    private  var mIMMessageList = ArrayList<QMMessage>()
    private lateinit var present: RoomMessagePresent

    companion object {

        const val EXITFORRESULT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_im)

        //房间信息
        mIMChatRoom=intent.getParcelableExtra<IMChatRoom>("imChatRoom")

        initRecycleView()

        im_bt.setOnClickListener(this)

        im_barview.setBarOnClickListener(this)
        //连接聊天室ws
        im_barview.changeTitle(mIMChatRoom.roomName.plus("(${mIMChatRoom.currentCount})"))

        mWebSocket = QMWebsocket()

        mWebSocket.connect(mIMChatRoom.roomNumber,this)

        present = RoomMessagePresent(this,this)

        getRoomMessage()
    }

    private fun getRoomMessage() {

        var user = App.instanceApp().getLocalUser()

        if (user != null){


            present.getRoomMessage(user.name,mIMChatRoom.roomNumber,bindToLifecycle())


        }else{

            val name = SPHelper.getInstance().get("loginName","") as String

            if (!TextUtils.isEmpty(name))
                present.getRoomMessage(name,mIMChatRoom.roomNumber,bindToLifecycle())
        }

    }

    private fun initRecycleView() {


        val linearLayoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_im.layoutManager=linearLayoutManager

        mIMListAdapter= IMListAdapter(this,mIMMessageList,this)

        recycleview_im.adapter = mIMListAdapter

        recycleview_im.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })
    }




    override fun onDestroy() {
        super.onDestroy()
        mWebSocket.close()
        MapLocationUtils.getInstance().exit()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.im_bt -> {


                        if (im_et.text.isEmpty())
                            return

                        val message = QMMessage()

                        message.message = im_et.text.toString()

                       /* mIMMessageList.add(message)

                        mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)

                        recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)*/

                        im_et.setText("")

                        mWebSocket.sengText(message,mIMChatRoom.roomNumber)


            }


        }
    }


    override fun imClick(content: QMMessage, type: Int) {

        when (type) {

            IMListAdapter.WHOCLICK-> {

                UiUtils.showToast("头像点击")

            }

            IMListAdapter.CONTENTCLICK -> {
                UiUtils.showToast("内容点击")
            }
        }


    }

    override fun qmMessage(message: QMMessage) {

        runOnUiThread {

            im_barview.changeTitle(mIMChatRoom.roomName.plus("(${message.currentCount})"))

            mIMMessageList.add(message)

            mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)
            //如果自己发的则活动不是则不管
            if (message.from == App.instanceApp().getLocalUser()?.name?:"xxx")

                recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)

            Logger.e(message.toString())
        }

    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {
                finish()
            }

            false -> {

                val intent = Intent(this, RoomInfoActivity::class.java)

                intent.putExtra("imChatRoom",mIMChatRoom)

                startActivityForResult(intent,EXITFORRESULT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            EXITFORRESULT -> {

               val boolean =  data?.getBooleanExtra("exitanddelete",false)

                if (boolean == true)
                    finish()
            }

        }

    }






    //请求来的是加密的密文需解密
    override fun setRoomMessage(messageList: List<QMMessage>) {

        var user =App.instanceApp().getLocalUser()
        if (user != null){

            for (qmMessage in messageList) {

                qmMessage.message = String(RSAUtils.decryptData(Base64.decode(qmMessage.message,0),RSAUtils.loadPrivateKey(user.privateKey))!!)
            }
        }else{

            UiUtils.showToast(getString(R.string.see_clear))
        }

        mIMMessageList.addAll(messageList)

        mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)

        if (mIMMessageList.size > 0)

            recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)


    }

    //请求记录
    override fun err(code: Int, errMessage: String?, type: Int) {

        UiUtils.showToast(errMessage!!)

    }

    override fun errorWS(type: Int, message: String) {

        runOnUiThread {

            when (type) {

                0,1 -> {
                    showProgressDialog(message,true)
                }

                2 -> {

                    dismissProgressDialog()
                }
            }



        }
    }


}
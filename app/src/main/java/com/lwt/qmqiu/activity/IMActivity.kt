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
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.QMMessage
import com.orhanobut.logger.Logger
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.RoomMessageContract
import com.lwt.qmqiu.mvp.present.RoomMessagePresent
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.ReporterDialog
import kotlinx.android.synthetic.main.activity_im.*


class IMActivity : BaseActivity(), View.OnClickListener, IMListAdapter.IMClickListen, QMWebsocket.QMMessageListen, BarView.BarOnClickListener, RoomMessageContract.View{



    private lateinit var mIMChatRoom:IMChatRoom
    private lateinit var mIMListAdapter:IMListAdapter
    private lateinit var mReporterDialogBuilder:ReporterDialog.Builder
    private lateinit var mReporterDialog:ReporterDialog
    private lateinit var mWebSocket: QMWebsocket
    private  var mIMMessageList = ArrayList<QMMessage>()
    private lateinit var present: RoomMessagePresent
    private  var mLocalUserName:String =SPHelper.getInstance().get("loginName","") as String

    private  var refuse = false

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
        im_barview.changeTitle((if (mIMChatRoom.roomType == 3)mIMChatRoom.roomName.replace("ALWTA","&") else mIMChatRoom.roomName).plus("(${mIMChatRoom.currentCount})"))

        mWebSocket = QMWebsocket()

        mWebSocket.connect(mIMChatRoom.roomNumber,this)

        present = RoomMessagePresent(this,this)

        getRoomMessage()

        //如果是私人聊天则检测是否阻止
        if (mIMChatRoom.roomType == 3){


            var info = mIMChatRoom.roomName.split("ALWTA")

            if (info.size == 2)

                //对方拒绝我没有
                present.refuseCheck(if (mLocalUserName == info[1])info[0] else info[1] ,mLocalUserName,bindToLifecycle())

        }

    }

    override fun setRefuseCheck(refuse: Boolean) {

        this.refuse = refuse

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

                        if (refuse){

                            UiUtils.showToast("对方拒绝接受,发送失败")

                            return
                        }

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

                val intent = Intent(this, UserInfoActivity::class.java)

                intent.putExtra("name",content.from)

                startActivity(intent)

            }

            IMListAdapter.CONTENTCLICK -> {

                //不能举报自己
                if (mLocalUserName == content.from)
                    return

                 mReporterDialogBuilder =  ReporterDialog.Builder(this,true)

                 mReporterDialog = mReporterDialogBuilder.create("举报",object :ReporterDialog.Builder.BtClickListen{

                    override fun btClick(type: Int): Boolean {

                        if (type == -1){

                            UiUtils.showToast("请选择举报内容")

                            return false

                        }

                        present.reportUser(mLocalUserName,content.from,type,mIMChatRoom.roomNumber,content.message,content.time,bindToLifecycle())

                        return true
                    }

                })

                mReporterDialog.show()

            }
        }


    }

    override fun qmMessage(message: QMMessage) {

        when (message.type) {
            //普通消息
            0 -> {

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
            //礼物
            2 -> {

                super.qmMessage(message)

            }
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

        mIMMessageList.addAll(messageList)

        mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)

        if (mIMMessageList.size > 0)

            recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)


    }

    //请求记录
    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {

            3 -> {
                mReporterDialogBuilder.btFinish(false)
            }

        }

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

    //举报成功
    override fun setReportUser(success: Boolean) {

        mReporterDialogBuilder.btFinish(success)

    }

}
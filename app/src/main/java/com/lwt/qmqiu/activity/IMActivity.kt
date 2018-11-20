package com.lwt.qmqiu.activity


import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.ClipboardManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.IMListAdapter
import com.lwt.qmqiu.adapter.PlusAdapter
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.PlusInfo
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.UploadLog
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.orhanobut.logger.Logger
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.RoomMessageContract
import com.lwt.qmqiu.mvp.present.RoomMessagePresent
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.Glide4Engine
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.voice.VoiceManager
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.ReporterDialog
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import kotlinx.android.synthetic.main.activity_im.*
import kotlinx.android.synthetic.main.layout_send_message_bar.*
import okhttp3.MediaType
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody






class IMActivity : BaseActivity(), View.OnClickListener, IMListAdapter.IMClickListen, QMWebsocket.QMMessageListen, BarView.BarOnClickListener, RoomMessageContract.View, PlusAdapter.PlusClickListen, View.OnTouchListener {

    private lateinit var mIMChatRoom:IMChatRoom
    private lateinit var mIMListAdapter:IMListAdapter
    private lateinit var mPlusAdapter:PlusAdapter
    private lateinit var mReporterDialogBuilder:ReporterDialog.Builder
    private lateinit var mReporterDialog:ReporterDialog
    private lateinit var mWebSocket: QMWebsocket
    private  var mIMMessageList = ArrayList<QMMessage>()
    private  var mPlusList = ArrayList<PlusInfo>()
    private lateinit var present: RoomMessagePresent
    private  var mLocalUserName:String =SPHelper.getInstance().get("loginName","") as String

    private  var refuse = false
    private  var voice = true
    private  var mSelected:List<Uri>?=null
    companion object {

        const  val REQUEST_CODE_CHOOSE = 110

        const val EXITFORRESULT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_im)

        //房间信息
        mIMChatRoom=intent.getParcelableExtra<IMChatRoom>("imChatRoom")

        initRecycleView()

        im_bt.setOnClickListener(this)

        voice_iv.setOnClickListener(this)

        send_voice_btn.setOnTouchListener(this)

        im_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                plus_iv.visibility = if (TextUtils.isEmpty(s))View.VISIBLE else View.GONE

                im_bt.visibility = if (TextUtils.isEmpty(s))View.GONE else View.VISIBLE

            }
        })

        //键盘监听
        KeyboardUtil.attach(this, panel_root
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
        ) { isShowing ->

            Logger.e("Keyboard is $isShowing")
        }

        //键盘收起
        KPSwitchConflictUtil.attach(panel_root, plus_iv, im_et) { switchToPanel ->


            if (switchToPanel) {

                if (!voice){
                    changeInput()
                    voice = true
                }

                im_et.clearFocus()

            } else {
                im_et.requestFocus()
            }
        }

        recycleview_im.setOnTouchListener { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root)
            }

            false
        }

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

        val gridLayoutManager = object :GridLayoutManager(this,4){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_plus.layoutManager=gridLayoutManager

        mPlusAdapter= PlusAdapter(this,mPlusList,this)

        recycleview_plus.adapter = mPlusAdapter

        recycleview_plus.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =10
            }
        })

        mPlusList.add(PlusInfo("相册",R.mipmap.photo))
        mPlusList.add(PlusInfo("拍摄",R.mipmap.camera))
        mPlusList.add(PlusInfo("视频通话",R.mipmap.video))
        mPlusList.add(PlusInfo("位置",R.mipmap.location))

        mPlusAdapter.notifyDataSetChanged()

    }

    override fun plusClick(position: Int) {


        when (position) {

            0 -> {

            Matisse.from(IMActivity@this)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(9)
                    .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(Glide4Engine())
                    .forResult(REQUEST_CODE_CHOOSE)
            }

            1 -> {

            }

            2 -> {

            }

            3 -> {

            }
        }

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
        voice_view.stopRecord()
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

                        message.type = 0
                        message.message = im_et.text.toString()

                       /* mIMMessageList.add(message)

                        mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)

                        recycleview_im.smoothScrollToPosition(mIMMessageList.size-1)*/

                        im_et.setText("")

                        mWebSocket.sengText(message,mIMChatRoom.roomNumber)


            }

            R.id.voice_iv ->{

                changeInput()

                voice = !voice

            }




        }
    }

    private fun changeInput() {

        if (voice) {

            voice_iv.setImageDrawable(getDrawable(R.mipmap.keybord))

            send_voice_btn.visibility = View.VISIBLE

            KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root)

            im_et.visibility = View.GONE

        } else {

            voice_iv.setImageDrawable(getDrawable(R.mipmap.voice_iv))

            send_voice_btn.visibility = View.GONE

            im_et.visibility = View.VISIBLE


        }

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v?.id) {

            R.id.send_voice_btn ->{

                var location = IntArray(2)

                send_voice_btn.getLocationOnScreen(location)
                var yY = location[1]
                var yHeight = send_voice_btn.bottom

                when (event?.action) {


                    MotionEvent.ACTION_DOWN-> {

                        send_voice_btn.isPressed = true

                        send_voice_btn.text = "松开发送"
                        //开始录音

                        voice_view.startRecord("${mIMChatRoom.roomNumber}_${System.currentTimeMillis()}",object :VoiceManager.VoiceRecordListen{

                            override fun start() {

                                Logger.e("开始录制")
                            }

                            override fun finished(file: File, time: Int) {

                                var user = App.instanceApp().getLocalUser()

                                if (user !=null){

                                    val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)

                                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                                    //type 为文件类型
                                    present.upload(user.name,0,mIMChatRoom.roomNumber,time,body,bindToLifecycle())

                                }else{

                                    UiUtils.showToast("未登录,无法发送")
                                }

                            }

                            override fun err(errMessage: String) {
                                UiUtils.showToast(errMessage)
                            }

                        })

                    }

                    MotionEvent.ACTION_UP-> {

                        voice_view.stopRecord( event.rawY>=yY-yHeight)

                        send_voice_btn.text = "按住说话"
                        send_voice_btn.isPressed = false

                    }

                    MotionEvent.ACTION_MOVE-> {
                        //修正一下yHeight个高度的误差

                        voice_view.setCancle(event.rawY<yY-yHeight)

                    }

                }

                return true
            }
        }

        return false
    }


    //上传成功
    override fun setUpload(uploadLog: UploadLog) {
        //上传成功后,发ws消息
        Logger.e("上传完成")

        val message = QMMessage()

        message.type = 3

        message.message = uploadLog._id.plus("_ALWTA_${uploadLog.length}")

        mWebSocket.sengText(message,mIMChatRoom.roomNumber)

    }


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP
                && event.keyCode == KeyEvent.KEYCODE_BACK
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun imClick(content: QMMessage, type: Int, longClick: Boolean) {

        when (type) {

            IMListAdapter.WHOCLICK-> {

                val intent = Intent(this, UserInfoActivity::class.java)

                intent.putExtra("name",content.from)

                startActivity(intent)

            }

            //消息内容点击
            IMListAdapter.CONTENTCLICK -> {

                if (longClick) {

                    mReporterDialogBuilder =  ReporterDialog.Builder(this,true)

                    mReporterDialog = mReporterDialogBuilder.create("选择",object :ReporterDialog.Builder.BtClickListen{

                        override fun btClick(index: Int, type: Int): Boolean {

                            when (type) {
                                //选择
                                0 -> {

                                    when (index) {

                                        0 -> {

                                            if (content.type ==0){

                                                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                // 将文本内容放到系统剪贴板里。
                                                cm.text = content.message

                                                UiUtils.showToast("复制成功",false)
                                            }else{

                                                UiUtils.showToast("只能复制文本信息",false)
                                            }


                                        }

                                        1 -> {

                                            mReporterDialogBuilder.initData(1,"举报")

                                        }
                                    }

                                }

                                1 -> {

                                    //不能举报自己
                                    if (mLocalUserName == content.from){

                                        UiUtils.showToast("无法举报自己")

                                        return false
                                    }



                                    if (index == -1){

                                        UiUtils.showToast("请选择举报内容")

                                        return false

                                    }

                                    present.reportUser(mLocalUserName,content.from,index,mIMChatRoom.roomNumber,content.message,content.time,bindToLifecycle())

                                    return true
                                }

                            }

                           return false
                        }

                    })

                    mReporterDialog.show()

                }else{
                    //点击
                    when (content.type) {

                        0 -> {

                            //普通消息点击
                        }

                        3 -> {
                            //进行下载和播放  将是否下载的文件用数据库做记录  id  name  path

                            var fileID = App.instanceApp().getShowMessage(content.message)
                            //文件下载
                            var params = fileID.split("_ALWTA_")

                            if (params.size>=2){


                                var down = DownloadManager(object :DownloadListen{
                                    override fun onStartDownload() {
                                        Logger.e("onStartDownload")
                                    }

                                    override fun onProgress(progress: Int) {
                                        Logger.e("onProgress:$progress")
                                    }

                                    override fun onFinishDownload(path: String) {

                                        voice_play.setVideoPath(path)
                                        voice_play.start()
                                        Logger.e("onFinishDownload:$path")
                                    }

                                    override fun onFail(errorInfo: String) {
                                        Logger.e("onFail:$errorInfo")
                                    }
                                },params[0])

                            }

                        }
                    }

                }


            }
        }


    }

    override fun qmMessage(message: QMMessage) {

        when (message.type) {
            //普通消息
            0,3-> {

                runOnUiThread {

                    im_barview.changeTitle(mIMChatRoom.roomName.plus("(${message.currentCount})"))

                    mIMMessageList.add(message)

                    mIMListAdapter.notifyItemChanged(mIMMessageList.size-1)
                    //如果自己发的则活动不是则不管
                    if (message.from == App.instanceApp().getLocalUser()?.name?:"xxx")

                        //需要顺滑
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

        if (resultCode != RESULT_OK)
            return

        when (requestCode) {

            EXITFORRESULT -> {

               val boolean =  data?.getBooleanExtra("exitanddelete",false)

                if (boolean == true)
                    finish()
            }

            REQUEST_CODE_CHOOSE  -> {

                mSelected = Matisse.obtainResult(data)

                Logger.e("mSelected: $mSelected")

            }

        }

    }






    //请求来的是加密的密文需解密
    override fun setRoomMessage(messageList: List<QMMessage>) {

        mIMMessageList.addAll(messageList)

        mIMListAdapter.notifyDataSetChanged()

        if (mIMMessageList.size > 0)

            //直接滑到
            recycleview_im.scrollToPosition(mIMMessageList.size-1)


    }

    //请求记录
    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {

            3 -> {
                mReporterDialogBuilder.btFinish(false)
            }
            4 -> {

                UiUtils.showToast("发送失败")

            }

        }

        Logger.e(errMessage)

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
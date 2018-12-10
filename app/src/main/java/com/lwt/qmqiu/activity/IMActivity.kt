package com.lwt.qmqiu.activity


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.*
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil
import cn.dreamtobe.kpswitch.util.KeyboardUtil
import com.google.gson.Gson
import com.guoxiaoxing.phoenix.compress.picture.internal.PictureCompressor
import com.guoxiaoxing.phoenix.compress.picture.listener.OnCompressListener
import com.guoxiaoxing.phoenix.compress.video.VideoCompressor
import com.guoxiaoxing.phoenix.compress.video.format.MediaFormatStrategyPresets
import com.guoxiaoxing.phoenix.core.PhoenixOption
import com.guoxiaoxing.phoenix.core.model.MimeType
import com.guoxiaoxing.phoenix.picker.Phoenix
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.IMListAdapter
import com.lwt.qmqiu.adapter.PlusAdapter
import com.lwt.qmqiu.bean.*
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.orhanobut.logger.Logger
import com.lwt.qmqiu.mvp.contract.RoomMessageContract
import com.lwt.qmqiu.mvp.present.RoomMessagePresent
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.shareelement.ShareContentInfo
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.voice.VoiceManager
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.ReporterDialog
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_im.*
import kotlinx.android.synthetic.main.layout_send_message_bar.*
import okhttp3.MediaType
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit


class IMActivity : BaseActivity(), View.OnClickListener, IMListAdapter.IMClickListen, QMWebsocket.QMMessageListen, BarView.BarOnClickListener, RoomMessageContract.View, PlusAdapter.PlusClickListen, View.OnTouchListener,IShareElements {



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
    private  var giftIndex = -1
    private  var mSeleteView:View? = null
    private  var mSeleteData:PhotoViewData? = null
    companion object {

        const  val REQUEST_CODE_CHOOSE_SELECT = 110
        const  val REQUEST_CODE_CHOOSE_TAKE = 220

        const val EXITFORRESULT = 1

        internal val REQUEST_CONTENT = 223
        internal val REQUEST_MAP = 249

        internal val MAX_RECORN_TIME = 15
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        YcShareElement.enableContentTransition(application)

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


        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this){
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

        recycleview_im.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })


        val gridLayoutManager = object : androidx.recyclerview.widget.GridLayoutManager(this,4){
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

        recycleview_plus.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
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

        if (mIMChatRoom.roomType == 3){

            var gift =App.instanceApp().getLocalUser()!!.gift.split("*")

            if (gift.size>=4){

                mPlusList.add(PlusInfo("天使", R.mipmap.angel,gift[0]))
                mPlusList.add(PlusInfo("玫瑰", R.mipmap.rose,gift[1]))
                mPlusList.add(PlusInfo("跑车", R.mipmap.paoche,gift[2]))
                mPlusList.add(PlusInfo("王冠", R.mipmap.kingset,gift[3]))

                gift_send.visibility = View.VISIBLE
            }

        }

        mPlusAdapter.notifyDataSetChanged()


        gift_send.text = "赠送礼物"
        gift_send.background = getDrawable(R.drawable.bg_20dp_13)
        gift_send.setFinalCornerRadius(20F)
        gift_send.setOnClickListener(this)

    }

    override fun plusClick(position: Int, obj: PlusInfo) {


        when (position) {

            //选择图片
            0 -> {

                Phoenix.with()
                        .theme(PhoenixOption.THEME_RED)// 主题
                        .fileType(MimeType.ofAll())//显示的文件类型图片、视频、图片和视频
                        .maxPickNumber(9)// 最大选择数量
                        .minPickNumber(0)// 最小选择数量
                        .spanCount(4)// 每行显示个数
                        .enablePreview(true)// 是否开启预览
                        .enableAnimation(true)// 选择界面图片点击效果
                        .enableCompress(true)// 是否开启压缩
                        .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                        .compressVideoFilterSize(1024*10)//多少kb以下的视频不压缩
                        .thumbnailHeight(160)// 选择界面图片高度
                        .thumbnailWidth(160)// 选择界面图片宽度
                        .enableClickSound(false)// 是否开启点击声音
                        .videoFilterTime(15)//显示多少秒以内的视频
                        .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                        .start(this@IMActivity, PhoenixOption.TYPE_PICK_MEDIA, REQUEST_CODE_CHOOSE_SELECT)

            }

            //拍摄
            1 -> {

                Phoenix.with()
                        .theme(PhoenixOption.THEME_RED)// 主题

                        .enablePreview(true)// 是否开启预览
                        .enableAnimation(true)// 选择界面图片点击效果
                        .enableCompress(true)// 是否开启压缩
                        .start(this@IMActivity, PhoenixOption.TYPE_TAKE_PICTURE, REQUEST_CODE_CHOOSE_TAKE)


            }

            //视频聊天
            2 -> {
                when (mIMChatRoom.roomType) {

                    1,2 -> {

                        showProgressDialog(getString(R.string.roomtype_video_notice))
                    }

                    3 -> {

                        val qmMessage = QMMessage()

                        //到什么房间号
                        qmMessage.to = mIMChatRoom.roomNumber

                        qmMessage.from = App.instanceApp().getLocalUser()?.name?:"xxx"

                        qmMessage.colorIndex = App.instanceApp().getLocalUser()?.colorIndex?:0

                        qmMessage.imgPath = App.instanceApp().getLocalUser()?.imgPath?:"qmqiuimg/nddzx.jpg"

                        qmMessage.type = 6

                        //将对象转为json
                        var gson = Gson()

                        present.videoRequest(qmMessage.from,qmMessage.to,gson.toJson(qmMessage),bindToLifecycle())

                       // Logger.e("请求参数:${gson.toJson(qmMessage)}")
                    }
                }

            }

            //位置
            3 -> {

                val intent = Intent(this, MapActivity::class.java)

                startActivityForResult(intent, REQUEST_MAP)

            }

            4,5,6,7 -> {

                if(obj.select)
                    this.giftIndex = position
                else
                    this.giftIndex = -1

            }
        }

    }

    //赠送礼物成功
    override fun setGiftSend(baseUser: BaseUser, giftIndex: Int) {

        App.instanceApp().updataLocalUser(baseUser,true)

        mPlusAdapter.changeGiftNum()

        gift_send.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        //动画
        showSuccessGift(giftIndex)

        Observable.timer(500, TimeUnit.MILLISECONDS).applySchedulers().subscribe({

            gift_send.revertAnimation()

        },{
            Logger.e("按钮复原异常")
        })


    }


    private fun showSuccessGift(giftIndex: Int) {

        var infoName = mIMChatRoom.roomName.split("ALWTA")

        var toName = if (infoName[0] == mLocalUserName)infoName[1] else infoName[0]


        //数量-单位-名称-动画名称

        val info = Html.fromHtml("$mLocalUserName(我)  赠送: <font color='#FF4081'>"+1+"</font>\t"+"${giftUnitList[giftIndex]}"+"<font color='#FF4081'>\t ${giftNameList[giftIndex]}</font>" +"\n给<font color='#FF4081'> $toName</font>")


        showGiftDialog(info,giftPathList[giftIndex])

    }

    override fun setVideoRequest(videoChannel: QMMessage) {

        var data = App.instanceApp().getShowMessage(videoChannel.message)


        val intent = Intent(this, FaceVideoActivity::class.java)

        intent.putExtra("videoChannel",data)
        intent.putExtra("active",false)
        intent.putExtra("message",videoChannel)
        startActivity(intent)

    }


    override fun onDestroy() {
        super.onDestroy()
        mWebSocket.close()

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

                        mWebSocket.sendText(message,mIMChatRoom.roomNumber)


            }

            R.id.voice_iv ->{

                changeInput()

                voice = !voice

            }

            R.id.gift_send ->{

                 var info = mIMChatRoom.roomName.split("ALWTA")

                if (mIMChatRoom.roomType == 3 && info.size==2){

                    if (giftIndex!=-1){

                        gift_send.startAnimation()

                        var toName = if (info[0] == mLocalUserName)info[1] else info[0]

                        //送给谁
                        present.giftSend(mLocalUserName,toName,giftIndex-4,1,bindToLifecycle())

                    }else{

                        UiUtils.showToast("请选择礼物")
                    }


                }

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

                                uploadFile(file,0,time)

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

    private fun sendMessage(type: Int,message:String){

        val qmMessage = QMMessage()

        qmMessage.type = type

        qmMessage.message = message

        mWebSocket.sendText(qmMessage,mIMChatRoom.roomNumber)


    }

    //上传成功
    override fun setUpload(uploadLog: UploadLog) {
        //上传成功后,发ws消息
        Logger.e("文件类型:${uploadLog.type}")
        when (uploadLog.type) {
            //语音文件

            0 -> {

                sendMessage(3,uploadLog._id.plus("_ALWTA_${uploadLog.length}"))

            }

            //图片
            1 -> {

                sendMessage(4,uploadLog._id.plus("_ALWTA_img"))

            }

            //视频
            2 -> {

                sendMessage(5,uploadLog._id.plus("_ALWTA_video"))

            }
        }


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

    override fun imClick(content: QMMessage, type: Int, longClick: Boolean, position: Int, view: View?, data: PhotoViewData?) {

        when (type) {

            //头像点击
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
                                                cm.text = App.instanceApp().getShowMessage(content.message)

                                                UiUtils.showToast("复制成功",false)
                                            }else{

                                                UiUtils.showToast("只能复制文本信息",false)
                                            }


                                            mReporterDialog.dismiss()
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

                                    mReporterDialog.dismiss()
                                    
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
                                },params[0],3)

                            }

                        }

                        4 -> {

                            //转场动画所需
                            this.mSeleteData = data
                            this.mSeleteView = view

                            val intent = Intent(this, PhotoViewActivity::class.java)

                            intent.putParcelableArrayListExtra("photoViewData",mIMListAdapter.getPhotoViewData())
                            intent.putExtra("index",position)


                            val options = YcShareElement.buildOptionsBundle(this, this)

                            startActivityForResult(intent, REQUEST_CONTENT, options)

                        }

                        5 -> {

                            //转场动画所需
                            this.mSeleteData = data
                            this.mSeleteView = view

                            val intent = Intent(this, VideoPlayActivity::class.java)

                            intent.putExtra("photoViewData",this.mSeleteData)

                            val options = YcShareElement.buildOptionsBundle(this, this)

                            startActivityForResult(intent, REQUEST_CONTENT, options)

                        }
                        //地图消息点击
                        8 -> {

                            var data = App.instanceApp().getShowMessage(content.message).split("_ALWTA_")[0]

                            var gson = Gson()

                            var location = gson.fromJson<LocationInfo>(data,LocationInfo::class.java)

                            Logger.e("地图点击:${content.message}")

                            val intent = Intent(this, MapInfoActivity::class.java)

                            intent.putExtra("locationinfo",location)

                            startActivity(intent)

                        }
                    }

                }


            }
        }


    }

    override fun qmMessage(message: QMMessage) {

        when (message.type) {
            //普通消息
            0,3,4,5,8-> {

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
            //礼物 视频聊天
            2 ,6-> {

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

            REQUEST_MAP -> {

               val locationInfo =  data?.getParcelableExtra<LocationInfo>("locationinfo")

                if (locationInfo != null){

                    var gson =Gson()

                    //地图信息
                    sendMessage(8,gson.toJson(locationInfo).plus("_ALWTA_map"))

                   Logger.e(locationInfo.toString())
                }
            }

            REQUEST_CODE_CHOOSE_SELECT  -> {

                val result = Phoenix.result(data)

                //在返回时需要对视频还是图片做区分,
                //TODO  1图片  2视频

                result.forEach {

                    Logger.e("it.finalPath:${it.finalPath } --- it.fileType:${it.fileType}")
                    uploadFile(File(it.finalPath),it.fileType)

                }

            }
            //需要自己压缩
            REQUEST_CODE_CHOOSE_TAKE  -> {

                val result = Phoenix.result(data)

                //在返回时需要对视频还是图片做区分,
                //TODO  1图片  2视频

                result.forEach {

                    when (it.fileType) {

                        1 -> {

                            var file = File(it.finalPath)

                            if (file.exists() && file.length()<=(1024*1024*1)){

                                uploadFile(file,it.fileType)

                            }else{

                                PictureCompressor.with(this)
                                        .load(file)
                                        .savePath(Environment.getExternalStorageDirectory().absolutePath+"/qmqiu")
                                        .setCompressListener(object :OnCompressListener {
                                            override fun onSuccess(file: File?) {

                                                var mLocalFile= file
                                                if (mLocalFile ==null)
                                                    mLocalFile = File(it.finalPath)

                                                uploadFile(mLocalFile,it.fileType)

                                            }

                                            override fun onError(e: Throwable?) {

                                                showProgressDialog("图片压缩异常")
                                            }

                                            override fun onStart() {

                                            }

                                        }).launch()
                            }


                        }

                        2 -> {

                            var file = File(it.finalPath)

                            if (file.exists() && file.length()<=(1024*1024*10)){

                                uploadFile(file,it.fileType)

                            }else{


                                var  compressFile:File
                                try {
                                    var compressCachePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "phoenix")

                                    if (!compressCachePath.exists())
                                        compressCachePath.mkdir()
                                    compressFile = File.createTempFile("compress", ".mp4", compressCachePath);
                                } catch ( e:IOException) {
                                    showProgressDialog("视频压缩异常")
                                    return
                                }

                                try {

                                    VideoCompressor.with().asyncTranscodeVideo(it.getLocalPath(), compressFile.getAbsolutePath(),
                                            MediaFormatStrategyPresets.createAndroid480pFormatStrategy(), object : VideoCompressor.Listener{
                                        override fun onTranscodeCompleted() {

                                            uploadFile(compressFile,it.fileType)
                                            dismissProgressDialog()
                                        }

                                        override fun onTranscodeProgress(progress: Double) {
                                            showProgressDialog("正在压缩 ${String.format("%.2f",(progress*100))}%")
                                        }

                                        override fun onTranscodeCanceled() {
                                            showProgressDialog("视频压缩异常")
                                        }

                                        override fun onTranscodeFailed(exception: Exception?) {
                                            showProgressDialog("视频压缩异常")
                                        }

                                    })
                                } catch ( e:IOException) {
                                    showProgressDialog("视频压缩异常")
                                    return
                                }


                            }


                        }
                    }

                    Logger.e("it.finalPath:${it.finalPath } --- it.fileType:${it.fileType}")


                }


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

            5 -> {

                UiUtils.showToast(if (code == 206)"用户不在线" else "发送视频聊天失败")

            }

            6 -> {

                gift_send!!.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(500,TimeUnit.MILLISECONDS).applySchedulers().subscribe({

                    gift_send.revertAnimation()

                },{
                    Logger.e("按钮复原异常")
                })

                Logger.e("礼物赠送失败")
                UiUtils.showToast("礼物赠送失败")

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

    //文件上传
    private fun  uploadFile(file: File,type: Int,time:Int = 0){

        var user = App.instanceApp().getLocalUser()

        if (user !=null){

            val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)

            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            //type 为文件类型
            present.upload(user.name,type,mIMChatRoom.roomNumber,if (type == 0) time else file.length().toInt(),body,bindToLifecycle())

        }else{

            UiUtils.showToast("未登录,无法发送")
        }

    }
    //转场动画

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

        YcShareElement.onActivityReenter(this, resultCode, data) {
            list ->

            recycleview_im.scrollToPosition((list[0].data as PhotoViewData).position)

        }

    }

    //转场动画所需
    override fun getShareElements(): Array<ShareElementInfo<PhotoViewData>> {

        return  arrayOf(ShareContentInfo(this.mSeleteView!!,this.mSeleteData))

    }


}
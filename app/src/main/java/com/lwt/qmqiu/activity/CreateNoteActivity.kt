package com.lwt.qmqiu.activity




import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.guoxiaoxing.phoenix.core.PhoenixOption
import com.guoxiaoxing.phoenix.core.model.MimeType
import com.guoxiaoxing.phoenix.picker.Phoenix
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.NotePhotoAdapter
import com.lwt.qmqiu.bean.LocationInfo
import com.lwt.qmqiu.bean.NoteMediaType
import com.lwt.qmqiu.bean.UploadLog
import com.lwt.qmqiu.mvp.contract.NoteCreateContract
import com.lwt.qmqiu.mvp.present.NoteCreatePresent
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.ItemView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_createnote.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class CreateNoteActivity : BaseActivity(),BarView.BarOnClickListener, NotePhotoAdapter.PhotoClickListen, ItemView.ItemOnClickListener, NoteCreateContract.View {


    companion object {

        internal val REQUEST_MAP = 249
        const  val REQUEST_CODE_CHOOSE_SELECT = 110
        val SEE_TYPE_All = "所有人"
        val SEE_TYPE_NEAR = "附近"
    }

    private var mNoteType = 0
    private var mFileNum = 0
    private lateinit var mPresenter: NoteCreatePresent
    private lateinit var mAdapter: NotePhotoAdapter
    private var mList: ArrayList<NoteMediaType> = ArrayList()
    private var mStringBuffer= StringBuffer()
    private var mLocationInfo:LocationInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_createnote)
        mNoteType=intent.getIntExtra("type",1)
        creatnote_barview.setBarOnClickListener(this)
        creatnote_barview.changeTitle("发表帖子")
        creatnote_barview.showMore(true)
        location.setBarOnClickListener(this,R.id.location)
        who_see.setBarOnClickListener(this,R.id.who_see)

        who_see.setShowContent(SEE_TYPE_All)
        mPresenter = NoteCreatePresent(this,this)
        initRv()
    }

    private fun initRv() {

        mList.add(NoteMediaType(""))

        //初始化list
        val linearLayoutManager = object : androidx.recyclerview.widget.GridLayoutManager(this,3){
            override fun canScrollVertically(): Boolean {
                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        rv_img.layoutManager=linearLayoutManager

        mAdapter= NotePhotoAdapter(this,mList,this)

        rv_img.adapter=mAdapter

        rv_img.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 2
                outRect.bottom = 1
                outRect.left = 2
                outRect.right = 0
            }
        })
    }

    override fun photoClick(position: Int, plus: Boolean) {

        if (plus){

            //为了流量考虑  只允许添加一个视频或图片
            Phoenix.with()
                    .theme(PhoenixOption.THEME_RED)// 主题
                    .fileType(MimeType.ofAll())//显示的文件类型图片、视频、图片和视频
                    .maxPickNumber(1)// 最大选择数量
                    .minPickNumber(0)// 最小选择数量
                    .spanCount(4)// 每行显示个数
                    .enableCamera(true)
                    .enablePreview(true)// 是否开启预览
                    .enableAnimation(true)// 选择界面图片点击效果
                    .enableCompress(true)// 是否开启压缩
                    .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                    .compressVideoFilterSize(1024*5)//多少kb以下的视频不压缩
                    .thumbnailHeight(160)// 选择界面图片高度
                    .thumbnailWidth(160)// 选择界面图片宽度
                    .enableClickSound(false)// 是否开启点击声音
                    .videoFilterTime(15)//显示多少秒以内的视频
                    .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                    .start(this@CreateNoteActivity, PhoenixOption.TYPE_PICK_MEDIA,REQUEST_CODE_CHOOSE_SELECT)

        }

    }

    override fun itemViewClick(id: Int) {

        when (id) {

            R.id.location -> {

                val intent = Intent(this, MapActivity::class.java)

                startActivityForResult(intent,REQUEST_MAP)
            }

        }
    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

            false -> {

                //检测文字是否为空
                showProgressDialog("请稍等",false,4)

                mFileNum = mList.size

                if (TextUtils.isEmpty(mList[0].path)){

                    var user = App.instanceApp().getLocalUser()

                    if (user !=null){

                        //type 为文件类型
                        mPresenter.createNote(user.name,mNoteType,if (who_see.getShowContent() == SEE_TYPE_NEAR) 1 else 2,"0",note_text.text.toString(),"",
                                if ( this.mLocationInfo == null) 0.00 else this.mLocationInfo!!.latitude,
                                if ( this.mLocationInfo == null) 0.00 else this.mLocationInfo!!.longitude,
                                if ( this.mLocationInfo == null) "" else "${this.mLocationInfo!!.locationWhere}-${this.mLocationInfo!!.locationName}",

                                bindToLifecycle())

                    }else{

                        UiUtils.showToast("未登录,无法发送")
                    }

                }else{

                    for (i in 0 until mList.size){

                        uploadFile(File(mList[0].path),mList[0].type)
                    }
                }


            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != RESULT_OK)
            return

        when (requestCode) {

            IMActivity.REQUEST_MAP -> {

                val success = data?.getBooleanExtra("success",false)

                if (success!!){

                    val locationInfo =  data.getParcelableExtra<LocationInfo>("locationinfo")

                    this.mLocationInfo = locationInfo

                    if (locationInfo != null){
                        location.setShowContent("${locationInfo.locationWhere}-${locationInfo.locationName}")
                        who_see.setShowContent(SEE_TYPE_NEAR)
                        Logger.e(locationInfo.toString())
                    }

                }else{

                    this.mLocationInfo = null

                    location.setShowContent("")

                    who_see.setShowContent(SEE_TYPE_All)

                }

            }

            REQUEST_CODE_CHOOSE_SELECT -> {

                val result = Phoenix.result(data)

                //在返回时需要对视频还是图片做区分,
                //TODO  1图片  2视频

                var list:ArrayList<NoteMediaType> = ArrayList<NoteMediaType>()

                result.forEach {


                    if(it.fileType == 1 && (it.localPath.endsWith(".gif") || it.localPath.endsWith(".GIG"))){

                        Logger.e("it.finalPath:${it.finalPath } --- it.fileType:${it.fileType}")
                        list.add(NoteMediaType(it.localPath,it.fileType))

                    }else{
                        Logger.e("it.finalPath:${it.finalPath } --- it.fileType:${it.fileType}")
                        list.add(NoteMediaType(it.finalPath,it.fileType))

                    }


                    Logger.e("it.finalPath:${it.finalPath } --- it.fileType:${it.fileType}")
                    Logger.e("it.localPath:${it.localPath } --- it.fileType:${it.fileType}")

                }

                if (list.size>0){
                    mList.clear()
                    mList.addAll(0,list)

                }

                mAdapter.notifyDataSetChanged()

            }

            }


    }

    override fun setCreateNote(success: Boolean) {
        dismissProgressDialog()
        if (success){

            UiUtils.showToast("发表帖子成功")

            finish()
        }

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {

            1 -> {
                dismissProgressDialog()
                UiUtils.showToast("发表帖子失败")
            }

            2 -> {
                //上传数
                mFileNum--

                if (mFileNum == 0)
                    dismissProgressDialog()
            }
        }
    }

    //文件上传
    private fun  uploadFile(file: File,type: Int,time:Int = 0){

        var user = App.instanceApp().getLocalUser()

        if (user !=null){

            val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)

            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            //type 为文件类型
            mPresenter.upload(user.name,type,user.name,file.length().toInt(),body,bindToLifecycle())

        }else{

            UiUtils.showToast("未登录,无法发送")
        }

    }

    override fun setUpload(uploadLog: UploadLog) {

        mFileNum--
        if (mFileNum == 0){
            mStringBuffer.append(uploadLog._id)

            var user = App.instanceApp().getLocalUser()

            if (user !=null){

                //type 为文件类型
                mPresenter.createNote(user.name,mNoteType,if (who_see.getShowContent() == SEE_TYPE_NEAR) 1 else 2,"${uploadLog.type}",note_text.text.toString(),mStringBuffer.toString(),
                        if ( this.mLocationInfo == null) 0.00 else this.mLocationInfo!!.latitude,
                        if ( this.mLocationInfo == null) 0.00 else this.mLocationInfo!!.longitude,
                        if ( this.mLocationInfo == null) "" else "${this.mLocationInfo!!.locationWhere}-${this.mLocationInfo!!.locationName}",

                        bindToLifecycle())

            }else{

                UiUtils.showToast("未登录,无法发送")
            }


            Logger.e(mStringBuffer.toString())
        }

        else
            mStringBuffer.append(uploadLog._id).append("_ALWTA_")

        Logger.e(uploadLog.toString())


    }

}
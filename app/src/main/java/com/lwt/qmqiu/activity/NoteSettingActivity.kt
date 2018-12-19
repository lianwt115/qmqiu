package com.lwt.qmqiu.activity




import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.NoteListAdapter
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.mvp.contract.NoteMineContract
import com.lwt.qmqiu.mvp.present.NoteMinePresent
import com.lwt.qmqiu.shareelement.ShareContentInfo
import com.lwt.qmqiu.utils.StaticValues
import com.lwt.qmqiu.widget.BarView
import com.lwt.qmqiu.widget.NoticeDialog
import com.lwt.qmqiu.widget.ReporterDialog
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_notesetting.*




class NoteSettingActivity : BaseActivity(),BarView.BarOnClickListener, NoteMineContract.View, NoteListAdapter.NoteClickListen, IShareElements {


    override fun getShareElements(): Array<ShareElementInfo<PhotoViewData>> {
        return  arrayOf(ShareContentInfo(this.mSeleteView!!,this.mSeleteData))
    }


    override fun setNoteMine(noteList: List<NoteLog>) {

        mNoteLogList.clear()

        mNoteLogList.addAll(noteList)

        mNoteLogList.sortBy {

            -it.creatTime
        }

        mAdapter.notifyDataSetChanged()

        if (noteList.isEmpty())

            showProgressDialog("您还没有发表帖子")
    }
    private  var mSeleteView:View? = null
    private  var mSeleteData: PhotoViewData? = null
    private lateinit var present: NoteMinePresent
    private  var mNoteLogList = ArrayList<NoteLog>()
    private lateinit var mAdapter: NoteListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notesetting)

        present = NoteMinePresent(this,this)

        notesetting_barview.setBarOnClickListener(this)
        notesetting_barview.changeTitle("我发表的")

        initRecycleView()
    }

    private fun initRecycleView() {


        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this){

        }
        recycleview_notesetting.layoutManager=linearLayoutManager

        mAdapter= NoteListAdapter(this,mNoteLogList,this,true)

        recycleview_notesetting.adapter = mAdapter

        recycleview_notesetting.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
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


    override fun noteClick(noteLog: NoteLog, position: Int, type: Int, view: View?) {
        when (type) {


            //图片或视频点击
            2 -> {
                //判断是图片还是视频

                when (noteLog.topic) {

                    //图片
                    "1" -> {

                        //转场动画所需
                        this.mSeleteData = PhotoViewData(position, noteLog.imgList)
                        this.mSeleteView = view

                        val intent = Intent(this, PhotoViewActivity::class.java)

                        intent.putParcelableArrayListExtra("photoViewData", arrayListOf(this.mSeleteData))
                        intent.putExtra("index", 0)
                        intent.putExtra("real", false)


                        val options = YcShareElement.buildOptionsBundle(this, this)

                        startActivityForResult(intent, IMActivity.REQUEST_CONTENT, options)

                    }


                    //视频
                    "2" -> {

                        //转场动画所需
                        this.mSeleteData = PhotoViewData(position, noteLog.imgList)
                        this.mSeleteView = view

                        val intent = Intent(this, VideoPlayActivity::class.java)

                        intent.putExtra("photoViewData", this.mSeleteData)

                        val options = YcShareElement.buildOptionsBundle(this, this)

                        startActivityForResult(intent, IMActivity.REQUEST_CONTENT, options)

                    }
                }

            }
            //删除
            6->{


                showProgressDialog("是否删除",true,2,object :NoticeDialog.Builder.BtClickListen{

                    override fun btClick(etContent: String): Boolean {

                        var localUser = App.instanceApp().getLocalUser()

                        if (localUser != null)
                            present.deleteNoteMine(localUser.name,noteLog._id!!,position,bindToLifecycle())

                        return true
                    }

                })


            }

        }

    }

    override fun setDeleteNoteMine(success: Boolean, position: Int) {

        showProgressDialogSuccess(true)

        mNoteLogList.removeAt(position)

        mAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()

        var localUser = App.instanceApp().getLocalUser()

        if (localUser != null)
            present.getNoteMine(localUser.name,bindToLifecycle())

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {
            //我的帖子获取
            1 -> {
            }
            //删除
            2 -> {
                showProgressDialogSuccess(false)
            }
        }
        Logger.e(errMessage?:"错误为空")
    }



}
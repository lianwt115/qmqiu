package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.graphics.Rect
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.CommentAdapter
import com.lwt.qmqiu.bean.NoteCommentLog
import com.lwt.qmqiu.utils.UiUtils


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class CommentDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context,private val cancle: Boolean) : DialogInterface.OnDismissListener, CommentAdapter.CommentClickListen, View.OnClickListener {


        private lateinit var mCommentDialog: CommentDialog
        private lateinit var mLayout: View
        private lateinit var mTitle: TextView
        private lateinit var mPublish: TextView
        private lateinit var mCommentEdit: EditText
        private lateinit var mExit: ImageView

        private lateinit var mCommentAdapter: CommentAdapter

        private lateinit var mRecyclerView: androidx.recyclerview.widget.RecyclerView

        private  var mNoteCommentLogList = ArrayList<NoteCommentLog>()
        private  var mListen:CommentClickListen? = null
        private  var mNoteId:String = ""


        fun create(data: List<NoteCommentLog>, listen: CommentClickListen?, id: String): CommentDialog {

            this.mNoteId = id
            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mCommentDialog = CommentDialog(mContext, R.style.GiftDialog)

            mCommentDialog.setCanceledOnTouchOutside(cancle)
            mCommentDialog.setOnDismissListener(this)
            mLayout = inflater.inflate(R.layout.dialog_comment, null)


            val display = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            var point = Point()
            display.defaultDisplay.getSize(point)

            //对话框全屏显示
            mCommentDialog.addContentView(mLayout, ViewGroup.LayoutParams(
                    point.x , point.y))

            //获取当前Activity所在的窗体
            var dialogWindow = mCommentDialog.window
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity( Gravity.BOTTOM)
            //获得窗体的属性
            var lp = dialogWindow.attributes
            lp.y = 0//设置Dialog距离底部的距离
            //       将属性设置给窗体
            dialogWindow.attributes = lp


            mTitle = mLayout.findViewById(R.id.title_comment) as TextView
            mPublish = mLayout.findViewById(R.id.publish) as TextView

            mExit = mLayout.findViewById(R.id.exit) as ImageView
            mCommentEdit = mLayout.findViewById(R.id.comment_et) as EditText

            mExit.setOnClickListener(this)
            mPublish.setOnClickListener(this)

            mRecyclerView = mLayout.findViewById(R.id.comment_rv) as androidx.recyclerview.widget.RecyclerView


            initRecycleView(data)


            initView(cancle,listen)

            return mCommentDialog as CommentDialog
        }


        private fun initRecycleView(data: List<NoteCommentLog>) {

            val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(mContext){
                override fun canScrollVertically(): Boolean {
                    return true
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            mRecyclerView.layoutManager=linearLayoutManager

            mCommentAdapter= CommentAdapter(mContext,mNoteCommentLogList,this)

            mRecyclerView.adapter = mCommentAdapter

            mRecyclerView.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                    outRect.top = 0
                    outRect.bottom = 0
                    outRect.left = 0
                    outRect.right =0
                }
            })

            initData(data)

        }

        fun initData(data:List<NoteCommentLog>,init:Boolean=true) {

             if (init)
                mNoteCommentLogList.clear()

             mNoteCommentLogList.addAll(0,data)

             mCommentAdapter.notifyDataSetChanged()

             mTitle.text = "全部 ${mNoteCommentLogList.size} 条评论"
        }

        fun addData(data:NoteCommentLog){

            initData(arrayListOf(data),false)
            mCommentEdit.setText("")
        }

        fun deleteData(position: Int){

            mNoteCommentLogList.removeAt(position)
            mCommentAdapter.notifyDataSetChanged()
            mTitle.text = "全部 ${mNoteCommentLogList.size} 条评论"
        }


        fun initView(cancle: Boolean,listen:CommentClickListen?) {

            this.mListen = listen

            mCommentDialog.setCanceledOnTouchOutside(cancle)

        }



        override fun onDismiss(dialog: DialogInterface?) {

        }

        override fun onClick(v: View?) {

            when (v?.id) {


                R.id.exit -> {
                    if (mListen!=null)
                        mListen?.commentClick(0,this.mNoteId)
                }

                R.id.publish -> {

                    if (TextUtils.isEmpty(mCommentEdit.text.toString()))
                        UiUtils.showToast("评论内容不能为空")
                    else
                        if (mListen!=null)
                            mListen?.commentClick(1,this.mNoteId,mCommentEdit.text.toString())
                }

            }
        }

        interface CommentClickListen{

            fun  commentClick(type:Int,id: String,data:Any?=null,position: Int=0)
        }

        override fun commentClick(noteCommentLog: NoteCommentLog, position: Int, type: Int, report: Boolean) {

            when (type) {
                //举报点击
                0 -> {
                    if (mListen!=null)
                        mListen?.commentClick(if (report) 2 else 3,this.mNoteId,noteCommentLog._id,position)
                }
            }
        }



    }
}
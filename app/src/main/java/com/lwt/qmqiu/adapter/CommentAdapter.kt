package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.NoteCommentLog
import com.lwt.qmqiu.network.ApiService
import java.text.SimpleDateFormat


class CommentAdapter(context: Context, list: List<NoteCommentLog>, listen: CommentClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<CommentAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: List<NoteCommentLog> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mCommentClickListen: CommentClickListen? = listen
    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter1 = SimpleDateFormat("MM月dd日")
    private val formatter2 = SimpleDateFormat("HH:mm")
    private val today = formatter.format(System.currentTimeMillis())

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        //头像 人名
        Glide.with(mContext).load(ApiService.BASE_URL_Api.plus(obj.fromImg)).into(holder.user_img)

        holder.user_name.text = obj.from

        holder.text_content.text = obj.commentContent

        var user = App.instanceApp().getLocalUser()

        if (user!=null && obj.from == user.name)
        {
            var draw = mContext.getDrawable(R.mipmap.delete1)
            draw.setBounds(0,0,draw.minimumWidth,draw.minimumHeight)
            holder.comment_report.setCompoundDrawables(draw,null,null,null)
            holder.comment_report.text = "删除"
        }

        holder.time.text = timeData(obj.commentTime)

        //举报点击
        holder.comment_report.setOnClickListener{
            if (this.mCommentClickListen!=null)
                mCommentClickListen?.commentClick(obj,position,0,holder.comment_report.text == "举报")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_comment, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var user_img: SelectableRoundedImageView = itemView.findViewById(R.id.user_img) as SelectableRoundedImageView
        var user_name: TextView = itemView.findViewById(R.id.user_name) as TextView


        var text_content: TextView = itemView.findViewById(R.id.text_content) as TextView

        var time: TextView = itemView.findViewById(R.id.time) as TextView
        var comment_report: TextView = itemView.findViewById(R.id.comment_report) as TextView


    }

    interface CommentClickListen{
        fun commentClick(noteCommentLog:NoteCommentLog, position: Int,type:Int,report:Boolean=true)
    }

    private fun timeData(currentTime :Long):String{

        var time = formatter.format(currentTime)

        return if (today.split("*")[0] == time.split("*")[0]){

            formatter2.format(currentTime)

        }else{
            formatter1.format(currentTime)
        }

    }

}
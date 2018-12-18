package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.lwt.qmqiu.network.ApiService
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat


class NoteListAdapter(context: Context, list: List<NoteLog>, listen: NoteClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<NoteListAdapter.ListViewHolder>() {

    private var mContext: Context = context
    private var mTotalList: List<NoteLog> = list
    private var inflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mNoteClickListen: NoteClickListen? = listen
    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter1 = SimpleDateFormat("MM月dd日")
    private val formatter2 = SimpleDateFormat("HH:mm")
    private val today = formatter.format(System.currentTimeMillis())
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        //头像 人名
        Glide.with(mContext).load(ApiService.BASE_URL_Api.plus(obj.nameImg)).into(holder.user_img)

        holder.user_name.text = obj.showName

        //内容图片
        //Glide.with(mContext).load(ApiService.BASE_URL_Api.plus(obj.nameImg)).into(holder.note_img)

        var  dataAll = obj.imgList.split("LWT")

        DownloadManager(object : DownloadListen {
            override fun onStartDownload() {
                Logger.e("onStartDownload")
            }

            override fun onProgress(progress: Int) {

                //Logger.e("onProgress:$progress")
            }

            override fun onFinishDownload(path: String, type: Int) {

                Glide.with(mContext).load(path).into(holder.note_img)

               /* holder.img_progress_text.visibility =View.GONE
                holder.img_progress.visibility =View.GONE
                Glide.with(context!!).load(if (obj.type == 4) path else getVideoThumbnail(path)).into(holder.photo_view)
                holder.videoplay_bg.visibility =if (obj.type == 4) View.GONE else View.VISIBLE*/

            }

            override fun onFail(errorInfo: String) {
                Logger.e("onFail:$errorInfo")
            }
        },dataAll[0],4)


        holder.text_content.text = obj.textContent

        holder.comment.text = "评论(${obj.commentNum})"

        holder.good.text = "点赞(${obj.goodNum})"

        holder.time.text = timeData(obj.creatTime)


        holder.user_img.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,0)
        }

        holder.user_name.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,1)

        }

        holder.note_img.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,2,holder.note_img)
        }

        holder.comment.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,3)
        }

        holder.good.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,4)
        }

        holder.report.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,5)
        }
        //绑定
        ViewCompat.setTransitionName(holder.note_img, dataAll[0])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater.inflate(R.layout.item_notelist, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var user_img: SelectableRoundedImageView = itemView.findViewById(R.id.user_img) as SelectableRoundedImageView
        var user_name: TextView = itemView.findViewById(R.id.user_name) as TextView


        var text_content: TextView = itemView.findViewById(R.id.text_content) as TextView
        var note_img: ImageView = itemView.findViewById(R.id.note_img) as ImageView

        var comment: TextView = itemView.findViewById(R.id.comment) as TextView
        var good: TextView = itemView.findViewById(R.id.good) as TextView
        var report: TextView = itemView.findViewById(R.id.report) as TextView
        var time: TextView = itemView.findViewById(R.id.time) as TextView

    }

    interface NoteClickListen{
        fun noteClick(noteLog:NoteLog, position: Int,type:Int,view:View?=null)
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
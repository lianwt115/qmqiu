package com.lwt.qmqiu.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.provider.MediaStore
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
import com.lwt.qmqiu.utils.StaticValues
import com.orhanobut.logger.Logger
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat


class NoteListAdapter(context: Context, list: List<NoteLog>, listen: NoteClickListen?,showType:Boolean=false) : androidx.recyclerview.widget.RecyclerView.Adapter<NoteListAdapter.ListViewHolder>() {

    var mTabs = StaticValues.mNoteTabs
    private var mContext: Context = context
    private var mTotalList: List<NoteLog> = list
    private var mVideoImg = HashMap<String,Bitmap>()
    private var inflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mNoteClickListen: NoteClickListen? = listen
    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter1 = SimpleDateFormat("MM月dd日")
    private val formatter2 = SimpleDateFormat("HH:mm")
    private val today = formatter.format(System.currentTimeMillis())
    private val showType = showType
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        //头像 人名
        Glide.with(mContext).load(ApiService.BASE_URL_Api.plus(obj.nameImg)).into(holder.user_img)

        holder.user_name.text = obj.showName

        //内容图片
        //Glide.with(mContext).load(ApiService.BASE_URL_Api.plus(obj.nameImg)).into(holder.note_img)

        var  dataAll = obj.imgList.split("LWT")

        //0只有文字  1 图片 2视频
        if (obj.topic == "0"){

            holder.note_img.visibility = View.GONE

        }else{

            //4图片  5视频
            DownloadManager(object : DownloadListen {
                override fun onStartDownload() {
                    Logger.e("onStartDownload")
                }

                override fun onProgress(progress: Int) {

                    //Logger.e("onProgress:$progress")
                }

                override fun onFinishDownload(path: String, type: Int) {
                    if (type == 4 && path.toLowerCase().endsWith(".gif")){

                        var gif = GifDrawable(path)

                        holder.note_img.setImageDrawable(gif)

                    }else{

                        Glide.with(mContext).load(if (type == 4) path else getVideoThumbnail(path)).into(holder.note_img)
                    }

                    holder.videoplay_bg.visibility =if (type == 4) View.GONE else View.VISIBLE

                }

                override fun onFail(errorInfo: String) {
                    Logger.e("onFail:$errorInfo")
                }
            },dataAll[0],if (obj.topic == "1") 4 else 5)

        }

        holder.location.text = "${obj.whereLocation}(附近)"

        holder.location.visibility = if (obj.seeType == 1) View.VISIBLE else View.GONE

        holder.note_type.text = mTabs[obj.noteType-1]
        holder.note_type.visibility = if (showType) View.VISIBLE else View.GONE

        holder.delete.visibility = if (showType) View.VISIBLE else View.GONE
        holder.report.visibility = if (showType) View.GONE else View.VISIBLE


        holder.location.visibility = if (obj.seeType == 1) View.VISIBLE else View.GONE

        holder.text_content.text = obj.textContent

        holder.comment.text = "评论(${obj.commentNum})"

        holder.good.text = "点赞(${obj.goodNum})"

       /* if (obj.hasGood){
            var draw = mContext.getDrawable(R.mipmap.good_active)
            draw.setBounds(0,0,draw.minimumWidth,draw.minimumHeight)
            holder.good.setCompoundDrawables(draw,null,null,null)
        }*/

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
        holder.delete.setOnClickListener{
            if (this.mNoteClickListen!=null)
                mNoteClickListen?.noteClick(obj,position,6)
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
        var note_img: GifImageView = itemView.findViewById(R.id.note_img) as GifImageView
        var videoplay_bg: ImageView = itemView.findViewById(R.id.videoplay_bg) as ImageView

        var comment: TextView = itemView.findViewById(R.id.comment) as TextView
        var good: TextView = itemView.findViewById(R.id.good) as TextView
        var report: TextView = itemView.findViewById(R.id.report) as TextView
        var time: TextView = itemView.findViewById(R.id.time) as TextView
        var location: TextView = itemView.findViewById(R.id.location) as TextView
        var note_type: TextView = itemView.findViewById(R.id.note_type) as TextView
        var delete: TextView = itemView.findViewById(R.id.delete) as TextView

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

    //获取视频文件缩略图
    fun  getVideoThumbnail( videoPath:String,change:Boolean =false,width:Int=0,height:Int=0): Bitmap {

        //缓存  防止视频图片闪
        if (mVideoImg[videoPath] != null) {

            return mVideoImg[videoPath]!!
        }

        var bitmap: Bitmap
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND) //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        if(bitmap!= null && change){
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT)//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        }

        mVideoImg[videoPath] = bitmap

        return bitmap
    }
}
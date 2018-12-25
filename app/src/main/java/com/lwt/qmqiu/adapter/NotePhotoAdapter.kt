package com.lwt.qmqiu.adapter

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.NoteMediaType



class NotePhotoAdapter(context: Context, list: ArrayList<NoteMediaType>, listen:PhotoClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<NotePhotoAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: ArrayList<NoteMediaType> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mPhotoClickListen: PhotoClickListen? = listen


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        when (obj.type) {

            1 -> {

                Glide.with(mContext).load(if (TextUtils.isEmpty(obj.path)) R.mipmap.add_photo else obj.path).into(holder.notephoto)
                holder.videoplay_bg.visibility = View.GONE
            }

            2 -> {

                Glide.with(mContext).load(getVideoThumbnail(obj.path)).into(holder.notephoto)
                holder.videoplay_bg.visibility = View.VISIBLE
            }
        }

        holder.notephoto.setOnClickListener {

            if (this.mPhotoClickListen!=null)
                this.mPhotoClickListen?.photoClick(position,TextUtils.isEmpty(obj.path))


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_notephoto, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var notephoto: ImageView = itemView.findViewById(R.id.notephoto) as ImageView
        var videoplay_bg: ImageView = itemView.findViewById(R.id.videoplay_bg) as ImageView

    }

    interface PhotoClickListen{
        fun photoClick(position: Int,plus:Boolean)
    }

    //获取视频文件缩略图
    fun  getVideoThumbnail( videoPath:String,change:Boolean =false,width:Int=0,height:Int=0): Bitmap {
        var bitmap: Bitmap
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND) //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        if(bitmap!= null && change){
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT)//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        }
        return bitmap
    }

}
package com.lwt.qmqiu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.VideoSurface



class VideoListAdapter(context: Context, list: ArrayList<VideoSurface>, listen:TextClickListen?) : RecyclerView.Adapter<VideoListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: ArrayList<VideoSurface>? = null
    var inflater: LayoutInflater? = null
    var listen: TextClickListen? = null

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)

        holder?.uid_tv?.text= if (obj!!.uid == 0) "自己" else obj!!.uid.toString()



        if (holder?.video_contain.childCount == 0) {

            val parent = obj!!.surface.parent
            if (parent != null) {
                (parent as FrameLayout).removeView(obj!!.surface)
            }

            holder?.video_contain.addView(obj!!.surface, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }


        if (listen !=null)

            holder?.uid_tv?.setOnClickListener {

                listen?.textClick(obj!!,position)

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_facevideoitem, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var uid_tv: TextView = itemView?.findViewById(R.id.uid_tv) as TextView
        var video_contain: FrameLayout = itemView?.findViewById(R.id.video_contain) as FrameLayout

    }

    interface TextClickListen{
        fun textClick(content:VideoSurface, position: Int)
    }

}
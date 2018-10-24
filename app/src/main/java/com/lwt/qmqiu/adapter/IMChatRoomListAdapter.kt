package com.lwt.qmqiu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.VideoSurface
import java.text.SimpleDateFormat


class IMChatRoomListAdapter(context: Context, list: ArrayList<IMChatRoom>, listen: RoomClickListen?) : RecyclerView.Adapter<IMChatRoomListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: ArrayList<IMChatRoom>? = null
    var inflater: LayoutInflater? = null
    var listen: RoomClickListen? = null

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)

        holder?.room_first.text = obj?.roomName?.substring(0,1)

        holder?.room_name.text = obj?.roomName

        holder?.room_lastcontent.text = obj?.lastContent

        holder?.room_time.text = timeData(obj?.lastContentTime!!)

        holder?.notice.visibility = if (obj?.status) View.INVISIBLE else View.VISIBLE


        holder?.root_contain.setOnClickListener {
            if (listen!= null)
                listen!!.roomClick(obj,position)
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_imchatroom, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var room_first: TextView = itemView?.findViewById(R.id.room_first) as TextView
        var room_name: TextView = itemView?.findViewById(R.id.room_name) as TextView
        var room_lastcontent: TextView = itemView?.findViewById(R.id.room_lastcontent) as TextView
        var room_time: TextView = itemView?.findViewById(R.id.room_time) as TextView
        var notice: ImageView = itemView?.findViewById(R.id.notice) as ImageView



        var root_contain: RelativeLayout = itemView?.findViewById(R.id.root_contain) as RelativeLayout

    }

    interface RoomClickListen{
        fun roomClick(content:IMChatRoom, position: Int)
    }

    private fun timeData(currentTime :Long):String{

        val formatter = SimpleDateFormat("HH:mm")

        return formatter.format(currentTime)

    }

}
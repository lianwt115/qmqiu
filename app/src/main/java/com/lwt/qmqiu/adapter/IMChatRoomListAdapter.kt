package com.lwt.qmqiu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.et_username
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.utils.UiUtils
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class IMChatRoomListAdapter(context: Context, list: ArrayList<IMChatRoom>, listen: RoomClickListen?) : RecyclerView.Adapter<IMChatRoomListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: ArrayList<IMChatRoom>? = null
    var inflater: LayoutInflater? = null
    var listen: RoomClickListen? = null
    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter1 = SimpleDateFormat("MM月dd日")
    private val formatter2 = SimpleDateFormat("HH:mm")
    private val today = formatter.format(System.currentTimeMillis())
    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)

        checkRoom(holder.room_first,obj?.roomName?.substring(0,1))

        holder.room_name.text = obj?.roomName

        holder.room_lastcontent.text = obj?.lastContent

        holder.room_time.text = timeData(obj?.lastContentTime!!)

        holder.notice.visibility = if (obj.status) View.INVISIBLE else View.VISIBLE

        roomFirstBg(obj.roomNumber,holder.room_first)

        holder.root_contain.setOnClickListener {
            if (listen!= null)
                listen!!.roomClick(obj,position)
        }




    }

    private fun checkRoom(text: TextView, roomName: String?) {

        var regEx = "[a-zA-Z]"

        var p = Pattern.compile(regEx)

        var m = p.matcher(roomName)

        if (m.matches()) {
            text.text =  roomName?.toUpperCase()
        }else{

            text.text = roomName
        }


    }

    private fun roomFirstBg(roomName: String, text: TextView) {

        var bg = context?.getDrawable(R.drawable.bubble_8dp)

        try {

            var num = roomName.substring(roomName.length-4).toInt()

            when(num%24) {

                1 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_1)
                }
                2 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_2)
                }
                3 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_3)
                }
                4 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_4)
                }
                5 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_5)
                }
                6 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_6)
                }
                7 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_7)
                }
                8 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_8)
                }
                9 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_9)
                }
                10 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_10)
                }
                11 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_11)
                }
                12 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_12)
                }
                13-> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_13)
                }
                14 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_14)
                }
                15 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_15)
                }
                16 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_16)
                }
                17 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_17)
                }
                18 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_18)
                }
                19 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_19)
                }
                20 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_20)
                }
                21 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_21)
                }
                22 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_22)
                }
                23 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_23)
                }
                0 -> {
                    bg =  context?.getDrawable(R.drawable.bubble_8dp_24)
                }

            }

            text.background = bg

        }catch (e:Exception){

            text.background = bg

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

        var room_first: TextView = itemView.findViewById(R.id.room_first) as TextView
        var room_name: TextView = itemView.findViewById(R.id.room_name) as TextView
        var room_lastcontent: TextView = itemView.findViewById(R.id.room_lastcontent) as TextView
        var room_time: TextView = itemView.findViewById(R.id.room_time) as TextView
        var notice: ImageView = itemView.findViewById(R.id.notice) as ImageView



        var root_contain: RelativeLayout = itemView.findViewById(R.id.root_contain) as RelativeLayout

    }

    interface RoomClickListen{
        fun roomClick(content:IMChatRoom, position: Int)
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
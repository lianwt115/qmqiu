package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.IMChatRoom
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class IMChatRoomListAdapter(context: Context, list: ArrayList<IMChatRoom>, listen: RoomClickListen?, mStrategy: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<IMChatRoomListAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: ArrayList<IMChatRoom> = list
    private var mSearchList: ArrayList<IMChatRoom> = ArrayList<IMChatRoom>()
    private var inflater: LayoutInflater =  LayoutInflater.from(mContext)
    private var mRoomClickListen: RoomClickListen? = listen
    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter1 = SimpleDateFormat("MM月dd日")
    private val formatter2 = SimpleDateFormat("HH:mm")
    private val today = formatter.format(System.currentTimeMillis())
    private val mType = mStrategy

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList.get(position)

        checkRoom(holder.room_first,obj.roomName.substring(0,1))

        holder.room_name.text = if (obj.roomType == 3)obj.roomName.replace("ALWTA","&") else obj.roomName

        var data = obj.lastContent.split("_ALWTA_")

        holder.room_lastcontent.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, null)

        if (data.size >= 2){

                var text:String

                when (data[1]) {

                    "img" -> {

                        text = "[图片]"

                    }

                    "video" -> {

                        text = "[视频]"

                    }

                    "videocall" -> {

                        text = "[视频聊天]"

                    }
                    "map" -> {

                        text = "[位置信息]"

                    }

                    else -> {

                        var drawableLeft = mContext.getDrawable(
                                R.mipmap.voice_type1)

                        holder.room_lastcontent.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                null, null, null)

                        holder.room_lastcontent.compoundDrawablePadding = 10


                        text = data[1].plus("s")

                    }
                }

                holder.room_lastcontent.text= text

        }else{

            holder.room_lastcontent.text = obj.lastContent

        }


        holder.room_time.text = timeData(if (mType == 4) obj.creatTime else obj.lastContentTime)

        roomFirstBg(obj.roomNumber,holder.room_first)

        if (mType == 3 || mType == 4)
            roomType(obj.roomType,holder.room_type)


        if (mSearchList.size>0 && position<=mSearchList.size-1){

            holder.room_type.visibility = View.VISIBLE

            holder.room_type.text = "搜索"

            holder.room_type.setTextColor(mContext.resources.getColor(R.color.colorAccent))

        }


        holder.root_contain.setOnClickListener {
            if (mRoomClickListen!= null)
                mRoomClickListen!!.roomClick(obj,position)
        }


    }

    private fun roomType(roomType: Int, room_type: TextView) {

        room_type.visibility = View.VISIBLE
        //1 附近 2公共 3私人
        when (roomType) {

            1-> {

                room_type.text = "附近"
            }

            2 -> {

                room_type.text = "推荐"
                room_type.setTextColor(mContext.resources.getColor(R.color.bg_start_color))
            }

            3 -> {

                room_type.text = "私密"

                room_type.setTextColor(mContext.resources.getColor(R.color.colorAccent))
            }
        }
    }

    fun  addSearchRoom(room:IMChatRoom){

        checkSearchCointain(room.roomName)

        mSearchList.add(0,room)

        mTotalList.addAll(0,mSearchList)

        notifyDataSetChanged()
    }

    fun refreshList(roomList: List<IMChatRoom>){

        mTotalList.clear()

        mTotalList.addAll(0,mSearchList)

        mTotalList.addAll(roomList)

        notifyDataSetChanged()
    }

    private fun  checkSearchCointain(name:String){

        mSearchList.forEach {

            if (it.roomName == name){

                mSearchList.remove(it)

                return@forEach
            }

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

        var bg = mContext.getDrawable(R.drawable.bubble_8dp)

        try {

            var num = roomName.substring(roomName.length-4).toInt()

            when(num%24) {

                1 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_1)
                }
                2 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_2)
                }
                3 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_3)
                }
                4 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_4)
                }
                5 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_5)
                }
                6 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_6)
                }
                7 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_7)
                }
                8 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_8)
                }
                9 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_9)
                }
                10 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_10)
                }
                11 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_11)
                }
                12 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_12)
                }
                13-> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_13)
                }
                14 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_14)
                }
                15 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_15)
                }
                16 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_16)
                }
                17 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_17)
                }
                18 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_18)
                }
                19 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_19)
                }
                20 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_20)
                }
                21 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_21)
                }
                22 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_22)
                }
                23 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_23)
                }
                0 -> {
                    bg =  mContext.getDrawable(R.drawable.bubble_8dp_24)
                }

            }

            text.background = bg

        }catch (e:Exception){

            text.background = bg

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater.inflate(R.layout.item_imchatroom, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var room_first: TextView = itemView.findViewById(R.id.room_first) as TextView
        var room_type: TextView = itemView.findViewById(R.id.room_type) as TextView
        var room_name: TextView = itemView.findViewById(R.id.room_name) as TextView
        var room_lastcontent: TextView = itemView.findViewById(R.id.room_lastcontent) as TextView
        var room_time: TextView = itemView.findViewById(R.id.room_time) as TextView


        var root_contain: RelativeLayout = itemView.findViewById(R.id.root_contain) as RelativeLayout

    }

    interface RoomClickListen{
        fun roomClick(content:IMChatRoom, position: Int)
    }

    private fun timeData(currentTime :Long):String{

        var time = formatter.format(currentTime)

        if (mType == 4)
            return time.replace("*"," ")

        return if (today.split("*")[0] == time.split("*")[0]){

            formatter2.format(currentTime)

        }else{
            formatter1.format(currentTime)
        }

    }

}
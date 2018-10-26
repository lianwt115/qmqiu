package com.lwt.qmqiu.adapter

import android.content.Context
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.VideoSurface
import java.text.SimpleDateFormat


class IMListAdapter(context: Context, list: List<QMMessage>, listen:IMClickListen?) : RecyclerView.Adapter<IMListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<QMMessage>? = null
    var inflater: LayoutInflater? = null
    var listen: IMClickListen? = null
    private lateinit var textTime:String
    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter2 = SimpleDateFormat("HH:mm:ss")
    private val formatter3 = SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss")

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

        this.textTime = "-1:-1:-1"

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)

        holder?.message_who?.text= obj?.from


        holder?.message_content?.text=obj?.message

        val messageTime= timeData(obj?.time!!)

        holder?.message_time?.text = messageTime

        //同一分钟就小时
        if (this.textTime.split(":")[1] == messageTime.split(":")[1])
            holder?.message_time?.visibility =View.GONE

        this.textTime = messageTime


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_immessage, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var message_who: TextView = itemView?.findViewById(R.id.message_who) as TextView
        var message_content: TextView = itemView?.findViewById(R.id.message_content) as TextView
        var message_time: TextView = itemView?.findViewById(R.id.message_time) as TextView

    }

    interface IMClickListen{
        fun imClick(content:VideoSurface, position: Int)
    }

    private fun timeData(currentTime :Long):String{

        var today =formatter.format(System.currentTimeMillis())


        var messageTime =formatter.format(currentTime)


        //是否是今天
        if (today.split("*")[0] == messageTime.split("*")[0]){


            return formatter2.format(currentTime)

        }else{

            return formatter3.format(currentTime)
        }


    }

}
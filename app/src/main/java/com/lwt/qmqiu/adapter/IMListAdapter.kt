package com.lwt.qmqiu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.utils.UiUtils
import java.text.SimpleDateFormat


class IMListAdapter(context: Context, list: List<QMMessage>, listen:IMClickListen?) : RecyclerView.Adapter<IMListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<QMMessage>? = null
    var inflater: LayoutInflater? = null
    var listen: IMClickListen? = null
    private  var textTime:String="-1:-1:-1"

    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter2 = SimpleDateFormat("HH:mm:ss")
    private val formatter3 = SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss")

    companion object {

          var WHOCLICK   =  0
          var CONTENTCLICK = 1
    }

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)


        Glide.with(context!!).load(ApiService.BASE_URL_Api.plus(obj?.imgPath)).into(holder.message_who)


        contentBg(obj!!.colorIndex,holder.message_content)

        holder.message_content.text=getShowMessage(obj.message)

        val messageTime= timeData(obj.time)

        holder.message_time.text = messageTime

        //同一分钟就小时
        if (this.textTime.split(":")[1] == messageTime.split(":")[1])
            holder.message_time.visibility =View.GONE

        this.textTime = messageTime

        holder.message_who.setOnClickListener {
            if (listen!=null)
                listen?.imClick(obj,WHOCLICK)
        }

        holder.message_content.setOnClickListener {
            if (listen!=null)
                listen?.imClick(obj,CONTENTCLICK)
        }


    }

    private fun getShowMessage(message: String):String{

        var user =App.instanceApp().getLocalUser()

        if (user != null){

            try{

                return String(RSAUtils.decryptData(Base64.decode(message,0), RSAUtils.loadPrivateKey(user.privateKey))!!)


            }catch (e:Exception){


                return message

            }


        }else{

            return message
        }

    }


    private fun contentBg(from: Int, text: TextView) {

        var bg = context?.getDrawable(R.drawable.bg_20dp_1)

        try {

            when(from%24) {

                1 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_1)
                }
                2 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_2)
                }
                3 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_3)
                }
                4 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_4)
                }
                5 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_5)
                }
                6 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_6)
                }
                7 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_7)
                }
                8 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_8)
                }
                9 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_9)
                }
                10 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_10)
                }
                11 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_11)
                }
                12 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_12)
                }
                13-> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_13)
                }
                14 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_14)
                }
                15 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_15)
                }
                16 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_16)
                }
                17 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_17)
                }
                18 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_18)
                }
                19 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_19)
                }
                20 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_20)
                }
                21 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_21)
                }
                22 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_22)
                }
                23 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_23)
                }
                0 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_24)
                }

            }

            text.background = bg

        }catch (e:Exception){

            text.background = bg

        }


    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_immessage, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var message_who: SelectableRoundedImageView = itemView?.findViewById(R.id.message_who) as SelectableRoundedImageView
        var message_content: TextView = itemView?.findViewById(R.id.message_content) as TextView
        var message_time: TextView = itemView?.findViewById(R.id.message_time) as TextView

    }

    interface IMClickListen{
        fun imClick(content:QMMessage, type: Int)
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
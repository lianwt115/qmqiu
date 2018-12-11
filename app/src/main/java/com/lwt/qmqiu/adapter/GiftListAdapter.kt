package com.lwt.qmqiu.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.utils.StaticValues
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class GiftListAdapter(context: Context, list: ArrayList<GiftLog>, mStrategy: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<GiftListAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: ArrayList<GiftLog> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val today = formatter.format(System.currentTimeMillis())
    private val mType = mStrategy
    protected val giftNameList = StaticValues.giftNameList

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        holder.gift_type.setImageDrawable(mContext.getDrawable(if (mType == 1 || mType == 3) R.mipmap.giftplus else R.mipmap.giftminus))

        holder.gift_time.text = timeData(obj.happenTime)

        holder.gift_cash.visibility = if ((mType == 1 && obj.type == 0) || (mType == 3 && obj.type == 2)) View.VISIBLE else View.GONE

        when (mType) {

            1 -> {

                holder.from_name.text = if (obj.type == 0) "购买" else "${obj.from}赠送"

                if (obj.type == 0){

                    holder.gift_cash.text = "花费:${obj.cash}青木球"

                }

            }

            2 -> {
                holder.from_name.text = "赠送给:${obj.to}"
            }

            3 -> {

                holder.from_name.text = "兑换"

                if (obj.type == 2){

                    holder.gift_cash.text = "兑换收入:${obj.cash}青木球"

                }

            }
        }

        holder.gift_content.text = showContent(obj.giftCount)


    }

    private fun showContent(giftCount: String): String {

        var giftCount = giftCount.split("*")

        var returnContent = StringBuilder()

        var appendCount = 0
        giftCount.forEachIndexed { index, s ->

            if (s != "0"){
                appendCount++
                returnContent.append(giftNameList[index].plus("x$s${if (appendCount==2)"\n" else ""}"))
            }

        }

        return returnContent.toString()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_giftlist, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var gift_type: ImageView = itemView.findViewById(R.id.gift_type) as ImageView
        var from_name: TextView = itemView.findViewById(R.id.from_name) as TextView
        var gift_content: TextView = itemView.findViewById(R.id.gift_content) as TextView
        var gift_time: TextView = itemView.findViewById(R.id.gift_time) as TextView
        var gift_cash: TextView = itemView.findViewById(R.id.gift_cash) as TextView

    }

    private fun timeData(currentTime :Long):String{

        return formatter.format(currentTime)

    }

}
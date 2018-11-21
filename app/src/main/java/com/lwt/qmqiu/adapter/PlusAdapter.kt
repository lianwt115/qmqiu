package com.lwt.qmqiu.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.GiftInfo
import com.lwt.qmqiu.bean.PlusInfo
import com.lwt.qmqiu.bean.ReportInfo
import com.lwt.qmqiu.network.ApiService


class PlusAdapter(context: Context, list: List<PlusInfo>, listen: PlusClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<PlusAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<PlusInfo>? = null
    var inflater: LayoutInflater? = null
    var listen: PlusClickListen? = null
    var index = -1

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)

        holder.plus_tv.text=obj?.content


        Glide.with(context!!).load(obj?.img).into(holder.plus_iv)

        holder.plus_iv.setOnClickListener {


            if (listen!=null)
                listen?.plusClick(position)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_plus, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var plus_iv: ImageView = itemView.findViewById(R.id.plus_iv) as ImageView
        var plus_tv: TextView = itemView.findViewById(R.id.plus_tv) as TextView

    }

 interface PlusClickListen{
        fun plusClick(position: Int)
    }



}
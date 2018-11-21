package com.lwt.qmqiu.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.GiftInfo


class GiftShowAdapter(context: Context, list: List<GiftInfo>, listen: GiftClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<GiftShowAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<GiftInfo>? = null
    var inflater: LayoutInflater? = null
    var listen: GiftClickListen? = null


    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)


        Glide.with(context!!).load(obj?.imgPath).into(holder.gift_img)


        holder.gift_count.text=obj?.count.toString()


        holder.gift_root.setOnClickListener {
            if (listen!=null)
                listen?.giftClick(obj!!,position)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_giftshow, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var gift_img: ImageView = itemView.findViewById(R.id.gift_img) as ImageView
        var gift_count: TextView = itemView.findViewById(R.id.gift_count) as TextView
        var gift_root: RelativeLayout = itemView.findViewById(R.id.gift_root) as RelativeLayout

    }

    interface GiftClickListen{
        fun giftClick(gift:GiftInfo, position: Int)
    }



}
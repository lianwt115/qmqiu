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


    private var mContext: Context = context
    private var mTotalList: List<PlusInfo> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var listen: PlusClickListen? = listen
    private var index = -1



    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        holder.plus_tv.text=obj.content

        holder.plus_iv.background = mContext.getDrawable(if (obj.select) R.drawable.bg_acc_rectangle_8 else R.drawable.bg_grey_rectangle_8)

        Glide.with(mContext).load(obj.img).into(holder.plus_iv)

        holder.plus_iv.setOnClickListener {

            if (position>=4 && position!=index){

                if (index>=4)
                    mTotalList[index].select =false

                obj.select = true

                index = position

                notifyDataSetChanged()
            }


            if (listen!=null)
                listen?.plusClick(position)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_plus, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var plus_iv: ImageView = itemView.findViewById(R.id.plus_iv) as ImageView
        var plus_tv: TextView = itemView.findViewById(R.id.plus_tv) as TextView

    }

 interface PlusClickListen{
        fun plusClick(position: Int)
    }



}
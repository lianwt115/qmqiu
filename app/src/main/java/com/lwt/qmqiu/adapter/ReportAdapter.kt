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
import com.lwt.qmqiu.bean.ReportInfo


class ReportAdapter(context: Context, list: List<ReportInfo>, listen: ReportClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<ReportAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<ReportInfo>? = null
    var inflater: LayoutInflater? = null
    var listen: ReportClickListen? = null
    var index = -1

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)

        holder.report_content.text=obj?.content

        holder.report_content.isSelected = obj?.selete!!

        holder.report_content.setOnClickListener {

            if (-1<index && index<mTotalList!!.size){

                mTotalList!![index].selete =false

            }

            index = position

            obj.selete = true

            if (listen!=null)
                listen?.reportClick(obj.content,position)

            notifyDataSetChanged()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_report, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var report_content: Button = itemView.findViewById(R.id.report_content) as Button

    }

    interface ReportClickListen{
        fun reportClick(report:String, position: Int)
    }



}